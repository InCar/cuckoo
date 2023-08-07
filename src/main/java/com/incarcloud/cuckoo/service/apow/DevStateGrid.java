package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;

// 国家电网
public class DevStateGrid implements IDev {

    private DevZLAN8308 devZLAN8308;

    private int PT = 1; // 电压变比 Potential Transformer
    private int CT = 60; // 电流变比 Current Transformer

    private final ACPhase[] tripleAC = new ACPhase[3]; // 三相交流电

    public DevStateGrid(String deviceID, String orgId, String tag) {
        this.devZLAN8308 = new DevZLAN8308(deviceID, ApowDevTypes.ElectricMeter, orgId, tag);

        for(int i=0;i<tripleAC.length;i++){
            tripleAC[i] = new ACPhase();
        }
    }

    public JsonNode toJsonNode(Instant tm){
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode data = mapper.createObjectNode();
        data.put("PT", this.PT);
        data.put("CT", this.CT);

        data.put("switchInOrOutStatus", 0);

        String[] arrPhase = {"A", "B", "C"};

        // 电压和电流都使用3位整数
        data.put("pointUAndI", 0x0303);
        data.put("pointPQ", 0);

        float[] fAC3 = new float[3];
        for(int i=0;i<tripleAC.length;i++){
            fAC3[i] = tripleAC[i].getVolatage();
        }

        // UA UB UC 相电压
        for(int i=0;i<tripleAC.length;i++){
            // 电压转成3位整数1位小数的定点数
            data.put("U" + arrPhase[i], (int)fAC3[i]*10);
        }
        // UAB UBC UAC 线电压
        data.put("UAB", (int)fAC3[0]*10*1.732f);
        data.put("UBC", (int)fAC3[1]*10*1.732f);
        data.put("UAC", (int)fAC3[2]*10*1.732f);

        // IA IB IC
        for(int i=0;i< tripleAC.length;i++){
            var acPhase = tripleAC[i];
            data.put("I" + arrPhase[i], (int)acPhase.getCurrent()*10);
        }

        // PA PB PC PTotal
        data.put("PA", 0);
        data.put("PB", 0);
        data.put("PC", 0);
        data.put("PTotal", 0);

        // QA QB QC QTotal
        data.put("QA", 0);
        data.put("QB", 0);
        data.put("QC", 0);
        data.put("QTotal", 0);

        // PFA PFB PFC PFTotal
        data.put("PFA", 1000);
        data.put("PFB", 1000);
        data.put("PFC", 1000);
        data.put("PFTotal", 1000);

        // SA SB SC STotal
        data.put("SA", 0);
        data.put("SB", 0);
        data.put("SC", 0);
        data.put("STotal", 0);

        // rateF
        data.put("rateF", 5000);

        // EPI EPE EqL EqC
        data.put("EPI", 40000800);
        data.put("EPE", 666480);
        data.put("EqL", 0);
        data.put("EqC", 2561340);

        return this.devZLAN8308.toJsonNode(tm, data);
    }

    @Override
    public byte[] makeDataPackage(Instant tm) {
        ObjectNode data = ApowJsonMapper.getMapper().createObjectNode();
        JsonNode root = this.toJsonNode(tm);
        try {
            return ApowJsonMapper.getMapper().writeValueAsBytes(root);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

class ACPhase{
    private float volatage; // 电压
    private float current; // 电流

    public ACPhase(){
        this.volatage = 220.0f;
        this.current = 0.0f;
    }

    public float getVolatage(){
        // 正负3%随机
        float delta = (float)(Math.random() * 0.06 - 0.03);
        return this.volatage * (1 + delta);
    }

    public float getCurrent(){
        // 0.0f~60.0f 随机波动
        float delta = (float)(Math.random() * 60.0f);
        return this.current + delta;
    }
}