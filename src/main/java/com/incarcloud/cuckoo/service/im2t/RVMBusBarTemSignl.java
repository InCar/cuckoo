package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

// 母线温度
public class RVMBusBarTemSignl {
    public static final int EvtID = 0xD00D;

    private static final int BMSBusTemplCount = 12; // EP33当前12个

    // 1字节数量+每个占2字节*12
    private static final int EvtLen = BMSBusTemplCount*2+1;

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            // 写入头部
            dos.writeShort(EvtID);
            dos.writeShort(EvtLen);

            dos.writeByte(BMSBusTemplCount);

            // 温度[-40, 215] 摄氏度
            int nTempl = 20;
            Random random = new Random();
            for(int i=0;i<BMSBusTemplCount;i++){
                // 略加随机变化
                int nT = nTempl + (int)(10 * (random.nextFloat() - 0.5f));
                dos.writeByte(nT+40);
                dos.writeByte(0x00); // 0 有效
            }

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
