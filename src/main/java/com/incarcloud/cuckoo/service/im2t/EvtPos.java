package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EvtPos {

    public static final int[] EvtID = { 0x0004, 0x0005, 0x0006 };
    private float fAlt = 0.0f; // 米[-500, +6000]
    private double dLon = 0.0; // 经度[-180, +180]
    private double dLat = 0.0; // 纬度[-90, +90]

    private int nStatus = 0; // 0:无信号 1:时间确定 2:2D定位 3:3D定位
    private float fAzimuth = 0.0f; // 方向角[0, 360)

    private float fHDop = 0.0f; // 水平精度因子[0, 10000]
    private float fVDop = 0.0f; // 垂直精度因子[0, 10000]

    public float getfAlt() {
        return fAlt;
    }

    public void setfAlt(float fAlt) {
        this.fAlt = fAlt;
    }

    public double getdLon() {
        return dLon;
    }

    public void setdLon(double dLon) {
        this.dLon = dLon;
    }

    public double getdLat() {
        return dLat;
    }

    public void setdLat(double dLat) {
        this.dLat = dLat;
    }

    public int getnStatus() {
        return nStatus;
    }

    public void setnStatus(int nStatus) {
        this.nStatus = nStatus;
    }

    public float getfAzimuth() {
        return fAzimuth;
    }

    public void setfAzimuth(float fAzimuth) {
        this.fAzimuth = fAzimuth;
    }

    public float getfHDop() {
        return fHDop;
    }

    public void setfHDop(float fHDop) {
        this.fHDop = fHDop;
    }

    public float getfVDop() {
        return fVDop;
    }

    public void setfVDop(float fVDop) {
        this.fVDop = fVDop;
    }

    public byte[] toBytes(){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream(8)) {
            var dos = new DataOutputStream(os);
            dos.writeShort(EvtID[0]);
            dos.writeShort((int)((fAlt+500.0f) * 10.0f));

            int nLon29Bits = (((int)(dLon*1.0e6))<<3);
            int nStatus2Bits = (nStatus & 0x03);
            dos.writeInt(nLon29Bits | nStatus2Bits);

            dos.writeShort(EvtID[1]);
            int nLat28Bits = (((int)(dLat*1.0e6))<<4);
            dos.writeInt(nLat28Bits); // 多写了4bits 0
            // TODO: 似乎新版2.0.6.8这4个bits写了个 VehTyp 0x1
            // dos.writeInt(nLat28Bits | 0x1);
            dos.writeShort((int)(fAzimuth*100.0f));

            dos.writeShort(EvtID[2]);
            int nHDop = (int)(fHDop*10.0f);
            dos.writeByte(nHDop>>16);
            dos.writeShort(nHDop & 0x0000FFFF);
            int nVDop = (int)(fVDop*10.0f);
            dos.writeByte(nVDop>>16);
            dos.writeShort(nVDop & 0x0000FFFF);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
}
