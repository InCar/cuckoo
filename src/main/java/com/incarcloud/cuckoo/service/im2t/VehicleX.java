package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.service.IDev;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Instant;

// 模拟一台车
public class VehicleX implements IDev {
    private String vin;

    public VehicleX(String vin){
        this.vin = vin;
    }

    // 根据时间生成数据包
    // 模拟器会快速生成数据包,例如,可能会在一分钟内模拟现实世界1小时的情况,并不按真实世界的时间
    // tm: GMT时间
    public byte[] makeDataPackage(Instant tm){
        EvtCRC32 evtCRC32 = new EvtCRC32();

        EvtTimTbox evtTimTbox = new EvtTimTbox();
        evtTimTbox.setTm(tm);
        byte[] buf1 = evtTimTbox.toBytes();
        evtCRC32.update(buf1);

        byte[] buf2 = evtCRC32.toBytes();

        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            dos.write(buf1);
            dos.write(buf2);

            JsonWrap jsonWrap = new JsonWrap(vin);
            return jsonWrap.toByteArray(tm, os.toByteArray());

        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
