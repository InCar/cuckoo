package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EvtOdoAccel {

    public static final int EvtID = 0x0803;

    private boolean bOdoMeterValid = true;
    private int nOdoMeter = 0; // [0, 16777215]km 24bits

    private boolean bBrakePosValid = true; // value is in Evt0x0801

    public boolean isOdoMeterValid(){ return bOdoMeterValid; }
    public void setOdoMeterValid(boolean valid){ bOdoMeterValid = valid; }

    public int getOdoMeter(){ return nOdoMeter; }
    public void setOdoMeter(int odo){ nOdoMeter = odo; }

    public boolean isBrakePosValid(){ return bBrakePosValid; }
    public void setBrakePosValid(boolean valid){ bBrakePosValid = valid; }

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);

            dos.writeByte(nOdoMeter >> 16);
            dos.writeShort(nOdoMeter & 0xFFFF);

            int b3 = (((bOdoMeterValid?0:1) << 7) | ((bBrakePosValid?0:1) << 6));
            dos.writeByte(b3);
            dos.writeShort(0);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

}
