package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.time.Instant;

public class EvtTimTbox {
    public static final int EvtID = 0x0001;

    private Instant tm;

    public Instant getTm() {
        return tm;
    }

    public void setTm(Instant tm) {
        this.tm = tm;
    }

    public byte[] toBytes(){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);
            long sec = tm.getEpochSecond();
            dos.writeShort((int)((0x0000FFFF00000000L & sec) >> 32));
            dos.writeInt((int)(0x00000000FFFFFFFFL & sec));
            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
