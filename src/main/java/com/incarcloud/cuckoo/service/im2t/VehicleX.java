package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.service.IDev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

// 模拟一台车
public class VehicleX implements IDev {
    private static final Logger s_logger = LoggerFactory.getLogger(VehicleX.class);
    private final String vin;
    private Instant tmStart = null; // 开始时间
    private Instant tm; // 最后一次更新的时间

    // the init coordinates of our office in Wuhan, China
    private double dLon = 114.40288538084347;
    private double dLat = 30.48121439124763;
    private float fAlt = 23.0f;
    private float fAzimuth = 82.0f;
    private float fSpeed = 15.0f; // m/s
    private float fMilage = 5000.0f; // km
    private JsonWrap jsonWrap;

    private EvtTimTbox evtTimTbox = new EvtTimTbox();
    private EvtPos evtPos = new EvtPos();
    private EvtVehPwrTemSts evtVehPwrTemSts = new EvtVehPwrTemSts();
    private EvtTirePrs evtTirePrs = new EvtTirePrs();
    private EvtBCMSts evtBCMSts = new EvtBCMSts();
    private EvtOdoAccel evtOdoAccel = new EvtOdoAccel();
    private RVMBaseSignl rvmBaseSignl = new RVMBaseSignl();
    private RVMCellVolSignl rvmCellVolSignl = new RVMCellVolSignl();
    private RVMCellTemSignl rvmCellTemSignl = new RVMCellTemSignl();
    private RVMBusBarTemSignl rvmBusBarTemSignl = new RVMBusBarTemSignl();

    public VehicleX(String vin, Instant tm){
        this.vin = vin;
        this.tm = tm;
        this.jsonWrap = new JsonWrap(vin);
    }

    public String getVin(){
        return this.vin;
    }

    public void setTm(Instant tmX){
        this.tm = tmX;
    }

    public void setPos(double dLon, double dLat){
        this.dLon = dLon;
        this.dLat = dLat;
    }

    public void setSpeed(float fSpeedKMPerHour){
        this.fSpeed = fSpeedKMPerHour * 1000.0f / 3600.0f;
    }

    public void update(Instant tmX){
        // 标记开始时间
        if(tmStart == null) tmStart = tmX;

        // 计算运动状态
        float fSeconds = (tmX.toEpochMilli() - this.tm.toEpochMilli())/1000.0f;
        double[] pos = calcNextPos(fSeconds);
        this.dLon = pos[0];
        this.dLat = pos[1];
        this.tm = tmX;

        // 计算里程
        this.fMilage += (fSpeed * fSeconds / 1000.0f);
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
                this.writeEvtTimTbox(dos, tmX, evtCRC32);

                this.update(tmX);
                this.writeEvtPosX3(dos, evtCRC32);

                this.writeEvtVehPwrTemSts(dos, evtCRC32);
                this.writeEvtTirePrs(dos, evtCRC32);
                this.writeEvtBCMSts(dos, evtCRC32);
                this.writeEvtOdoAccel(dos, evtCRC32);

                // 变长数据包
                this.writeRVMBaseSignl(dos, evtCRC32);
                this.writeRVMCellVolSignl(dos, evtCRC32);
                this.writeRVMCellTemSignl(dos, evtCRC32);
                this.writeRVMBusBarTemSignl(dos, evtCRC32);
            }

            // 最后加上CRC32
            buf = evtCRC32.toBytes();
            dos.write(buf);

            return this.jsonWrap.toByteArray(tm, os.toByteArray());

        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private void writeEvtTimTbox(DataOutputStream dos, Instant tmX, EvtCRC32 evtCRC32) throws IOException {
        evtTimTbox.setTm(tmX);
        byte[] buf = evtTimTbox.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeEvtPosX3(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        evtPos.setdLon(dLon);
        evtPos.setdLat(dLat);
        evtPos.setfAlt(fAlt);
        evtPos.setfAzimuth(fAzimuth);
        evtPos.setnStatus(3);
        evtPos.setfHDop(5.0f);
        evtPos.setfVDop(5.0f);
        byte[] buf = evtPos.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeEvtVehPwrTemSts(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException{
        // 12V电压就绪
        evtVehPwrTemSts.setSysVoltageValid(true);
        evtVehPwrTemSts.setSysVoltage(12.0f);
        // 主电源就绪
        evtVehPwrTemSts.setSysPowerModeValid(true);
        evtVehPwrTemSts.setSysPowerMode(2); // 0-Off 1-Accessories 2-Run 3-CrankREQ
        // 变速箱
        evtVehPwrTemSts.setShiftLevelPosValid(true);
        evtVehPwrTemSts.setShiftLevelPos(10); // 0换档 1P 2R 3N 4~11D 15Unknown

        byte[] buf = evtVehPwrTemSts.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeEvtTirePrs(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException{
        evtTirePrs.setBrakePos(0.0f); // [0.0, 100.0]百分比
        byte[] buf = evtTirePrs.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeEvtBCMSts(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException{
        evtBCMSts.setSpeedValid(true);
        evtBCMSts.setSpeed(fSpeed * 3.6f); // km/h

        byte[] buf = evtBCMSts.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeEvtOdoAccel(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        evtOdoAccel.setBrakePosValid(true);
        evtOdoAccel.setOdoMeterValid(true);
        evtOdoAccel.setOdoMeter((int)fMilage);

        byte[] buf = evtOdoAccel.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeRVMBaseSignl(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        byte[] buf = rvmBaseSignl.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeRVMCellVolSignl(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        byte[] buf = rvmCellVolSignl.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeRVMCellTemSignl(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        byte[] buf = rvmCellTemSignl.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }

    private void writeRVMBusBarTemSignl(DataOutputStream dos, EvtCRC32 evtCRC32) throws IOException {
        byte[] buf = rvmBusBarTemSignl.toBytes();
        evtCRC32.update(buf);
        dos.write(buf);
    }
}
