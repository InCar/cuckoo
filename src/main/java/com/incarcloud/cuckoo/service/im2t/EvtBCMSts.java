package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EvtBCMSts {

    public static final int EvtID = 0x0802;

    private boolean bSpeedValid = true;
    private float fSpeed = 0.0f; // [0.0, 512.0)km/h

    public boolean isSpeedValid(){ return bSpeedValid; }
    public void setSpeedValid(boolean valid){ bSpeedValid = valid; }

    public float getSpeed(){ return fSpeed; }
    public void setSpeed(float speed){ fSpeed = speed; }

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);

            int u16 = ((int)(fSpeed * 64.0f) << 1) | ( bSpeedValid ? 0 : 1);
            dos.writeShort(u16);

            dos.writeInt(0);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
