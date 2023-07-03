package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class RVMCellTemSignl {
    public static final int EvtID = 0xD00C;

    private static final int BMSTemplCount = 12; // EP33当前12个

    // 1字节数量+每个占2字节*12
    private static final int EvtLen = BMSTemplCount*2+1;


    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            // 写入头部
            dos.writeShort(EvtID);
            dos.writeShort(EvtLen);

            dos.writeByte(BMSTemplCount);

            // 温度[-40, 215] 摄氏度
            int nTempl = 20;
            Random random = new Random();
            for(int i=0;i<BMSTemplCount;i++){
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
