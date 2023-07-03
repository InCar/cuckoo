package com.incarcloud.cuckoo.service.im2t;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RVMBaseSignl extends RVMSignl{
    public static final int EvtID = 0xD006;

    // 内容固定62字节(不含头部EvtID[2字节]+EvtLen[2字节]
    private static final int EvtLen = 62;

    public RVMBaseSignl(){
        super(EvtLen);
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
