package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.service.IDev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

// 模拟一台车
public class VehicleX implements IDev {
    private static final Logger s_logger = LoggerFactory.getLogger(VehicleX.class);
    private final String vin;
    private Instant tm;

    // the init coordinates of our office in Wuhan, China
    private double dLon = 114.40288538084347;
    private double dLat = 30.48121439124763;
    private float fAlt = 23.0f;
    private float fAzimuth = 82.0f;
    private float fSpeed = 15.0f; // m/s
    private JsonWrap jsonWrap;

    private EvtTimTbox evtTimTbox = new EvtTimTbox();
    private EvtPos evtPos = new EvtPos();

    public VehicleX(String vin, Instant tm){
        this.vin = vin;
        this.tm = tm;
        this.jsonWrap = new JsonWrap(vin);
    }

    public String getVin(){
        return this.vin;
    }

    public void update(Instant tmX){
        // 计算运动状态
        float fSeconds = (tmX.toEpochMilli() - this.tm.toEpochMilli())/1000.0f;
        double[] pos = calcNextPos(fSeconds);
        this.dLon = pos[0];
        this.dLat = pos[1];
        this.tm = tmX;
    }

    // 根据时间生成数据包
    // 模拟器会快速生成数据包,例如,可能会在一分钟内模拟现实世界1小时的情况,并不按真实世界的时间
    // tm: GMT时间
    public byte[] makeDataPackage(Instant tm){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);

            byte[] buf = null;
            EvtCRC32 evtCRC32 = new EvtCRC32();

            // 10组数据一个数据包
            final int COUNT = 10;
            // 每个数据包的间隔1秒
            for(int i=0;i<COUNT;i++){
                Instant tmX = tm.minusSeconds(COUNT-i);

                evtTimTbox.setTm(tmX);
                buf = evtTimTbox.toBytes();
                evtCRC32.update(buf);
                dos.write(buf);

                this.update(tmX);
                evtPos.setdLon(dLon);
                evtPos.setdLat(dLat);
                evtPos.setfAlt(fAlt);
                evtPos.setfAzimuth(fAzimuth);
                evtPos.setnStatus(3);
                evtPos.setfHDop(5.0f);
                evtPos.setfVDop(5.0f);
                buf = evtPos.toBytes();
                evtCRC32.update(buf);
                dos.write(buf);
            }

            // 最后加上CRC32
            buf = evtCRC32.toBytes();
            dos.write(buf);

            return this.jsonWrap.toByteArray(tm, os.toByteArray());

        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private double[] calcNextPos(float fSeconds) {
        double dEarthRadius = 6371000.0f;

        // Convert degrees to radians
        double initialLongitudeRad = Math.toRadians(this.dLon);
        double initialLatitudeRad = Math.toRadians(this.dLat);
        double azimuthRad = Math.toRadians(this.fAzimuth);

        // Convert speed to angular distance
        double angularDistance = (fSpeed * fSeconds) / dEarthRadius;

        // Calculate change in latitude and longitude
        double deltaLatitude = angularDistance * Math.cos(azimuthRad);
        double deltaLongitude = angularDistance * Math.sin(azimuthRad) / Math.cos(initialLatitudeRad);

        // Calculate final latitude and longitude
        double finalLatitude = Math.toDegrees(initialLatitudeRad + deltaLatitude);
        double finalLongitude = Math.toDegrees(initialLongitudeRad + deltaLongitude);

        // Return the final position as an array
        double[] finalPosition = { finalLongitude, finalLatitude };
        return finalPosition;
    }
}
