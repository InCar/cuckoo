package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;

public class DevDCDC implements IDev {

    private final DevZLAN8308 devZLAN8308;

    public DevDCDC(String deviceID, String orgId, String tag){
        this.devZLAN8308 = new DevZLAN8308(deviceID, ApowDevTypes.DCDC, orgId, tag);
    }

    public JsonNode toJsonNode(Instant tm){
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("outputStatus", 0);
        data.put("inputStatus", 0);
        data.put("equipmentStatus", 1);
        data.put("operateMode", 4);
        data.put("controlStatus", 0);
        data.put("faultInfo1", 0);
        data.put("faultInfo2", 0);
        data.put("faultInfo4", 0);
        data.put("faultInfo5", 0);
        data.put("busV", 8304);
        data.put("busA", 0);
        data.put("positiveBusV", 0);
        data.put("negativeBusV", 0);
        data.put("power", 0);
        data.put("machineBatteryV", 7662);
        data.put("machineBatteryA", 0);
        data.put("batteryFautInfo1", 0);
        data.put("batteryFautInfo2", 0);
        data.put("batteryTotalV", 7657);
        data.put("batteryTotalA", 0);
        data.put("batterySOC", 72);
        data.put("batterySOH", 100);
        data.put("batteryType", 3);
        data.put("batteryCapacity", 0);
        data.put("batteryTemp", 38);
        data.put("batteryChargeCount", 0);
        data.put("batterySingleLowV", 3304);
        data.put("batterySingleLowVNum", 46);
        data.put("batterySingleHignV", 3331);
        data.put("batterySingleHignVNum", 1);
        data.put("batterySingleLowTemp", 24);
        data.put("batterySingleLowTempNum", 104);
        data.put("batterySingleHighTemp", 27);
        data.put("batterySingleHighTempNum", 8);
        data.put("batteryChargeTotalElect", 0);
        data.put("batteryDischargeTotalElect", 0);
        data.put("dcdcEquipmentStatus", 1);
        data.put("dcdcMasterSlaveStatus", 1);
        data.put("dcdcWorkMode", 4);
        data.put("dcdcFautInfo1", 0);
        data.put("dcdcFautInfo2", 0);
        data.put("dcdcBusbarV", 8261);
        data.put("dcdcBusbarA", 0);
        data.put("dcdcPositiveBusbarV", 0);
        data.put("dcdcNegativeBusbarV", 0);
        data.put("dcdcActivePower", -10);
        data.put("dcdcBatteryV", 0);
        data.put("dcdcBatteryA", -1);
        data.put("npbStatus", 31296);
        data.put("npbFautInfo1", 0);
        data.put("npbFautInfo2", 0);
        data.put("npbPositiveBusbarV", 62670);
        data.put("npbNegativeBusbarV", 31296);
        data.put("npbTotalBusbarV", 256);
        data.put("npbA", 0);
        data.put("npbPower", 0);
        data.put("paramWorkMode", 4);
        data.put("paramCVOutputV", 8400);
        data.put("paramCVModeLimitCurrent", 320);
        data.put("paramCOPILOTModePower", 100);
        data.put("paramBusSideV", 8400);

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
