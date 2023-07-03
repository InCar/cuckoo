package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class RVMCellVolSignl{
    public static final int EvtID = 0xD00B;

    private static final int BMSCellCount = 108; // EP33当前108个
    // 1字节数量+每个占2字节*108
    private static final int EvtLen = BMSCellCount*2+1;

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            // 写入头部
            dos.writeShort(EvtID);
            dos.writeShort(EvtLen);

            dos.writeByte(BMSCellCount);

            // 电压[0~8.191] 单体电压 伏特
            float fVoltage = 4.0f;
            Random random = new Random();
            for(int i=0;i<BMSCellCount;i++){
                // 微小的随机变化
                float fV = fVoltage + fVoltage * (random.nextFloat() - 0.5f) * 0.1f;
                boolean bValid = true;
                int writeValue = (((int)(fV * 1000.0f) << 3) | (( bValid ? 0x00: 0x01) << 2));
                dos.writeShort(writeValue);
            }

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
