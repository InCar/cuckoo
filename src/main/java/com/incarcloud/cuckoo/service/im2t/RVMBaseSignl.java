package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RVMBaseSignl {
    public static final int EvtID = 0xD006;

    // 内容固定62字节(不含头部EvtID[2字节]+EvtLen[2字节]
    private static final int EvtLen = 62;

    private byte[] _buf = new byte[EvtLen];

    public RVMBaseSignl(){

    }

    public byte[] toBytes() {
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            // 写入头部
            dos.writeShort(EvtID);
            dos.writeShort(EvtLen);

            // 改写内容
            this.writeValid(bEPTTrInptShaftToqV, 106, 1);
            this.writeBuf((int)((fEPTTrInptShaftToq+848.0f)*2.0f), 102, 12);

            this.writeValid(bEPTBrkPdlDscrtInptStsV, 123, 1);
            this.writeBuf(bEPTBrkPdlDscrtInptSts, 124, 1);

            this.writeBuf(bEPBSysBrkLghtsReqd, 121, 1);
            this.writeBuf(bBrkSysBrkLghtsReqd, 122, 1);

            // 写入内容
            dos.write(_buf, 0, EvtLen);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public byte[] toBytesTestOnly(){
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            var dos = new DataOutputStream(os);
            // 写入头部
            dos.writeShort(EvtID);
            dos.writeShort(EvtLen);

            // 改写内容
            this.writeBuf(0xff, 38, 8);

            // 写入内容
            dos.write(_buf, 0, EvtLen);

            return os.toByteArray();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    // 按智己的bit索引定义,从指定的位置开始,写入指定长度(bits)
    // value: 要写入的值
    // nStartBit: 起始bit索引
    // SignalLength: 信号长度(bits),由于value是int,所以最大值不能超过32
    private void writeBuf(int value, int nStartBit, int SignalLength){
        // bit索引定义来自于智己文档
        // 07 06 05 04 03 02 01 00
        // 15 14 13 12 11 10 09 08
        // 23 22 21 20 19 18 17 16
        // ...
        // 转换为对字节的操作
        int nStartBitNorm = toNormalOrder(nStartBit-32);
        int nEndBitNorm = nStartBitNorm + SignalLength - 1;
        // 按字节计算
        int nStartByte = nStartBitNorm / 8;
        int nEndByte = nEndBitNorm / 8;

        int shiftBits = SignalLength;
        for(int i=nStartByte;i<=nEndByte;i++){
            // 计算掩码,要写入的位置1
            int nBitFrom = i * 8;
            int nMaskV = 0x80; // b1000 0000
            int nMask = 0;
            for(int j=0;j<8;j++){
                int nBit = nBitFrom + j;
                if(nBit >= nStartBitNorm && nBit <= nEndBitNorm) {
                    nMask |= nMaskV;
                    shiftBits--;
                }
                else if(nBit > nEndBitNorm){
                    shiftBits--;
                }
                nMaskV >>= 1;
            }
            // 计算写入数值
            byte maskValue;
            if(shiftBits >= 0)
                maskValue = (byte)((value >> shiftBits) & nMask);
            else
                maskValue = (byte)((value << -shiftBits) & nMask);

            this._buf[i] = (byte)((this._buf[i] & ~nMask) | maskValue);
        }
    }

    private int toNormalOrder(int nStartBit){
        // 转换为正常的顺序
        // 00 01 02 03 04 05 06 07
        // 08 09 10 11 12 13 14 15
        // 16 17 18 19 20 21 22 23
        // ...
        int m = nStartBit % 8;
        int n = nStartBit / 8;
        return n * 8 + (7 - m);
    }

    private void writeBuf(boolean value, int nStartBit, int SignalLength){
        writeBuf(value?1:0, nStartBit, SignalLength);
    }

    private void writeValid(boolean value, int nStartBit, int SignalLength){
        // 注意,有效无效跟平常的习惯是相反的
        // 0x0=Valid 0x1=Invalid
        writeBuf(value?0:1, nStartBit, SignalLength);
    }


    // 变速箱输入扭矩
    private boolean bEPTTrInptShaftToqV = false;
    private float fEPTTrInptShaftToq = 0.0f; // [-848.0, 1199.5] Nm

    // 电子驻车
    private boolean bEPTBrkPdlDscrtInptStsV = false;
    private boolean bEPTBrkPdlDscrtInptSts = false;
    private boolean bEPBSysBrkLghtsReqd = false;

    // 刹车灯
    private boolean bBrkSysBrkLghtsReqd = false;
}
