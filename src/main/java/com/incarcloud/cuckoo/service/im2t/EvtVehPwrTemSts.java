package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EvtVehPwrTemSts {
    public static final int EvtID = 0x0800;

    // system power mode validity 系统上电状态有效性
    private boolean bSysPwrMdV = true;

    // system power mode 系统上电状态
    private int nSysPwrMd = 0; // 0-Off 1-Accessories 2-Run 3-CrankREQ

    // 变速器档位有效性
    private boolean bTrShiftLvrPosV = true;
    // transmission shift level position 变速器档位
    private int nTrShiftLvrPos = 0; // 0换档 1P 2R 3N 4~11D 15Unknown

    // 12V蓄电池电压有效性
    private boolean bSysVolV = true;
    // system voltage 12V蓄电池电压
    private float fSysVol = 12.0f; // [3.0, 28.5]

    public boolean isSysPowerModeValid(){ return bSysPwrMdV; }
    public void setSysPowerModeValid(boolean valid){ bSysPwrMdV = valid; }

    public int getSysPowerMode(){ return nSysPwrMd; }
    public void setSysPowerMode(int mode){ nSysPwrMd = mode; }

    public boolean isShiftLevelPosValid(){ return bTrShiftLvrPosV; }
    public void setShiftLevelPosValid(boolean valid){ bTrShiftLvrPosV = valid; }
    public int getShiftLevelPos(){ return nTrShiftLvrPos; }
    public void setShiftLevelPos(int pos){ nTrShiftLvrPos = pos; }

    public boolean isSysVoltageValid(){ return bSysVolV; }
    public void setSysVoltageValid(boolean valid){ bSysVolV = valid; }
    public float getSysVoltage(){ return fSysVol; }
    public void setSysVoltage(float vol){ fSysVol = vol; }


    public byte[] toBytes(){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID);

            int b0 = ((nTrShiftLvrPos << 6) | ((bSysPwrMdV?0:1) << 5) | ((bSysVolV?0:1) << 4) | nTrShiftLvrPos );
            dos.writeByte(b0);

            int b1 = (int)((fSysVol - 3.0f) * 10.0f);
            dos.writeByte(b1);

            // b2~b4
            dos.writeByte(0);
            dos.writeByte(0);
            dos.writeByte(0);

            int b5 = ((bTrShiftLvrPosV?0:1) << 7);
            dos.writeByte(b5);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
