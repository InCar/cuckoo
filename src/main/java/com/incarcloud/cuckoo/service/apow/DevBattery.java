package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;

public class DevBattery implements IDev {
    private final DevZLAN8308 devZLAN8308;

    public DevBattery(String deviceID, String orgId, String tag){
        this.devZLAN8308 = new DevZLAN8308(deviceID, ApowDevTypes.Battery, orgId, tag);
    }
    public JsonNode toJsonNode(Instant tm) {
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("basicStatus", 1027);
        data.put("protectInfo", 0);
        data.put("alarmInfo", 0);
        data.put("totalV", 7656);
        data.put("currentA", 0);
        data.put("temperature", 390);
        data.put("soc", 71);
        data.put("cycleTime", 11);
        data.put("pileMaxChargeV", 8280);
        data.put("pileMaxChargeA", 14800);
        data.put("pileMinDischargeV", 6670);
        data.put("pileMaxDischargeA", -14800);
        data.put("switchValue", 3);
        data.put("maxCellV", 3333);
        data.put("minCellV", 3304);
        data.put("serialVMaxCell", 1);
        data.put("serialVMinCell", 33);
        data.put("maxCellTemp", 270);
        data.put("minCellTemp", 240);
        data.put("serialTempMaCell", 2);
        data.put("serialTempMinCell", 120);
        data.put("maxModuleV", 3331);
        data.put("minModuleV", 3324);
        data.put("serialVMaxModule", 6);
        data.put("serialVMinModule", 21);
        data.put("maxModuleTemp", 270);
        data.put("minModuleTemp", 246);
        data.put("serialTempMaxModule", 22);
        data.put("serialTempMinModule", 12);
        data.put("soh", 100);
        data.put("remainCapacity", 81820);
        data.put("chargeCapacity", 80460);
        data.put("dischargeCapacity", 46069);
        data.put("dailyChargeCapacity", 80460);
        data.put("dailyDischargeCapacity", 46069);
        data.put("historyChargeCapacity", 1407);
        data.put("historyDischargeCapacity", 1301);
        data.put("forceChargeMark", 0);
        data.put("balanceChargeMark", 1);
        data.put("errorCode1", 0);
        data.put("errorCode2", 0);
        data.put("moduleNumSeries", 23);
        data.put("cellNumSeries", 230);
        data.put("chargeForbiddenMark", 0);
        data.put("disChargeForbiddenMark", 0);
        data.put("moduleNumV1", 3330);
        data.put("moduleNumV2", 3330);
        data.put("moduleNumV3", 3328);
        data.put("moduleNumV4", 3328);
        data.put("moduleNumV5", 3328);
        data.put("moduleNumV6", 3329);
        data.put("moduleNumV7", 3331);
        data.put("moduleNumV8", 3327);
        data.put("moduleNumV9", 3324);
        data.put("moduleNumV10", 3327);
        data.put("moduleNumV11", 3326);
        data.put("moduleNumV12", 3329);
        data.put("moduleNumV13", 3328);
        data.put("moduleNumV14", 3328);
        data.put("moduleNumV15", 3331);
        data.put("moduleNumV16", 3327);
        data.put("moduleNumV17", 3331);
        data.put("moduleNumV18", 3330);
        data.put("moduleNumV19", 3326);
        data.put("moduleNumV20", 3328);
        data.put("moduleNumV21", 3329);
        data.put("moduleNumV22", 3324);
        data.put("moduleNumV23", 3331);
        data.put("moduleNumTemp1", 267);
        data.put("moduleNumTemp2", 260);
        data.put("moduleNumTemp3", 260);
        data.put("moduleNumTemp4", 260);
        data.put("moduleNumTemp5", 260);
        data.put("moduleNumTemp6", 260);
        data.put("moduleNumTemp7", 260);
        data.put("moduleNumTemp8", 256);
        data.put("moduleNumTemp9", 257);
        data.put("moduleNumTemp10", 250);
        data.put("moduleNumTemp11", 252);
        data.put("moduleNumTemp12", 250);
        data.put("moduleNumTemp13", 246);
        data.put("moduleNumTemp14", 250);
        data.put("moduleNumTemp15", 250);
        data.put("moduleNumTemp16", 254);
        data.put("moduleNumTemp17", 256);
        data.put("moduleNumTemp18", 260);
        data.put("moduleNumTemp19", 260);
        data.put("moduleNumTemp20", 260);
        data.put("moduleNumTemp21", 260);
        data.put("moduleNumTemp22", 260);
        data.put("moduleNumTemp23", 270);

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
