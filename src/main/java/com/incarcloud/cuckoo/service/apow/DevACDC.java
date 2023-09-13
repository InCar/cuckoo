package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;

public class DevACDC implements IDev {

    private final DevZLAN8308 devZLAN8308;

    public DevACDC(String deviceID, String orgId, String tag){
        this.devZLAN8308 = new DevZLAN8308(deviceID, ApowDevTypes.ACDC, orgId, tag);
    }

    public JsonNode toJsonNode(Instant tm) {
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("outputStatus", 0);
        data.put("inputstatus", 0);
        data.put("machineDeviceStatus", 1);
        data.put("machineWorkModel", 1);
        data.put("controlStatus", 1);
        data.put("machineFaultInfo1", 0);
        data.put("machineFaultInfo2", 0);
        data.put("machineFaultInfo3", 0);
        data.put("machineFaultInfo4", 0);
        data.put("machineUPhaseV", 2269);
        data.put("machineUPhaseA", 26);
        data.put("machineVPhaseV", 2275);
        data.put("machineVPhaseA", 27);
        data.put("machineWParseV", 2270);
        data.put("machineWParseA", 27);
        data.put("machineFrequence", 4999);
        data.put("machineActivePower", 1);
        data.put("machineReactivePower", -18);
        data.put("machineInspectPower", 18);
        data.put("machinePowerFactor", 78);
        data.put("machineDCV", 10);
        data.put("machineDCA", 0);
        data.put("machineDCPower", 0);
        data.put("npbStatus", 0);
        data.put("npbFault", 0);
        data.put("npbPositiveBusbarV", 0);
        data.put("npbNegativeBusarV", 0);
        data.put("npbBusarTotalV", 58517);
        data.put("npbA", 20032);
        data.put("npbPower", 16469);
        data.put("paramWorkMode", 1);
        data.put("paramDCOutputV", 8300);
        data.put("paramConstantFlow", 300);
        data.put("paramACActiveValue", 2500);
        data.put("paramPowerChange", 0);
        data.put("paramCrossflowA", 300);
        data.put("paramACReactiveValue", 0);
        data.put("paramDroopCoefficient", 0);
        data.put("paramACV", 2300);
        data.put("paramACRate", 5000);
        data.put("paramReactiveValue", 0);
        data.put("paramDCPower", 0);

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
