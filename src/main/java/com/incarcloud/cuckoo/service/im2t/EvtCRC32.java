package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
public class EvtCRC32 {
    public static final int EvtID = 0xFFFF;
    private CRC32 crc32 = new CRC32();

    public void update(byte[] buf){
        crc32.update(buf);
    }

    public byte[] toBytes(){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);
            dos.writeShort(0);
            dos.writeInt((int)crc32.getValue());
            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
