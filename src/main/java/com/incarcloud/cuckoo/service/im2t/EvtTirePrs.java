package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EvtTirePrs {
    public static final int EvtID = 0x0801;

    private float fBrakePos = 0.0f; // [0.0, 100.0]

    public float getBrakePos(){ return fBrakePos; }
    public void setBrakePos(float pos){ fBrakePos = pos; }

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);

            dos.writeInt(0);
            dos.writeByte(0);
            dos.writeByte((int)(fBrakePos * 255.0f/100.0f));

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
