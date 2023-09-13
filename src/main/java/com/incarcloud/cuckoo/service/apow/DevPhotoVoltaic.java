package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;

public class DevPhotoVoltaic implements IDev {
    private final DevZLAN8308 devZLAN8308;

    public DevPhotoVoltaic(String deviceID, String orgId, String tag){
        this.devZLAN8308 = new DevZLAN8308(deviceID, ApowDevTypes.PhotoVoltaic, orgId, tag);
    }

    public JsonNode toJsonNode(Instant tm){
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode data = mapper.createObjectNode();

        data.put("gridOvervoltage", 0);
        data.put("gridUndervoltage", 0);
        data.put("gridOverfrequency", 0);
        data.put("gridUnderfrequency", 0);
        data.put("gridReverseConnection", 0);
        data.put("offGrid", 1);
        data.put("gridImbalance", 0);
        data.put("gridFrequencyJitter", 0);
        data.put("gridOvercurrent", 0);
        data.put("gridCurrentTrackingFault", 0);
        data.put("dcOvervoltage", 0);
        data.put("dcBusOvervoltage", 0);
        data.put("unevenDCBusVoltage", 0);
        data.put("dcBusUndervoltage", 0);
        data.put("unevenDCBusVoltage2", 0);
        data.put("dcAWayPassingCurrent", 0);
        data.put("dcBWayPassingCurrent", 0);
        data.put("dcInputDisturbance", 0);
        data.put("dcReverseConnection", 0);
        data.put("pvNeutralGround", 0);
        data.put("gridDisturbance", 0);
        data.put("dspInitializationProtection", 0);
        data.put("overTempProtection", 0);
        data.put("pvInsulationFailure", 0);
        data.put("leakageProtection", 0);
        data.put("relayDetectionProtection", 0);
        data.put("dspbProtection", 0);
        data.put("dcComponentLarge", 0);
        data.put("undervoltageProtection12V", 0);
        data.put("leakageCurrentSelfTestProtection", 0);
        data.put("underTempProtection", 0);
        data.put("arcSelfTestProtection", 0);
        data.put("arcFault", 0);
        data.put("gridDisturbance2", 0);
        data.put("gridCurrentAbnormal", 0);
        data.put("igbtOvercurrent", 0);
        data.put("normalOperation", 0);
        data.put("initialStandby", 0);
        data.put("controlShutdown", 0);
        data.put("downtime", 1);
        data.put("standby", 0);
        data.put("derating", 0);
        data.put("quotaOperation", 0);
        data.put("overloadBuck", 0);
        data.put("gridSurge", 0);
        data.put("fanFailure", 0);
        data.put("productNumber", 208);
        data.put("dspSoftwareVersion", 42);
        data.put("lcdSoftwareVersion", 3);
        data.put("acOutputType", 3);
        data.put("dcInputType", 7);
        data.put("activePower", 0);
        data.put("totalDCOutputPower", 0);
        data.put("totalPowerGeneration", 1707);
        data.put("monthlyPowerGeneration", 37);
        data.put("powerGenerationLastMonth", 66);
        data.put("powerGenerationOfTheDay", 245);
        data.put("yesterdayPowerGeneration", 0);
        data.put("powerGenerationThisYear", 126);
        data.put("powerGenerationLastYear", 0);
        data.put("dcV1", 7653);
        data.put("dcA1", 0);
        data.put("dcV2", 7586);
        data.put("dcA2", 0);
        data.put("dcV3", 13);
        data.put("dcA3", 0);
        data.put("dcV4", 7);
        data.put("dcA4", 0);
        data.put("iinitializeGroundVoltageValue", 698);
        data.put("dcBusV", 7573);
        data.put("dcBusHalfV", 3786);
        data.put("abLineV", 0);
        data.put("bcLineV", 0);
        data.put("caLineV", 0);
        data.put("aPhaseA", 0);
        data.put("bPhaseA", 0);
        data.put("cPhaseA", 0);
        data.put("masterSlaveDSPUpgradeClosure", 0);
        data.put("standardWorkingMode", 1);
        data.put("inverterTemp", 277);
        data.put("gridFrequency", 0);
        data.put("inverterCurrentStatus", 4117);
        data.put("activePowerRegulationOutput", 26750);
        data.put("reactivePowerRegulationOutput", 0);
        data.put("inverterControlWord", 4);
        data.put("actualPowerValue", 10000);
        data.put("powerFactorActualAdjusstValue", 1000);
        data.put("powerFactorActualAdjusstValue2", 1000);
        data.put("limitReactivePowerValue", 0);
        data.put("nationalStandard", 25);
        data.put("powerCurveNumber", 0);
        data.put("reactivePowerValue", 0);
        data.put("apparentPowerValue", 0);
        data.put("inverterSn1", 53281);
        data.put("inverterSn2", 37120);
        data.put("inverterSn3", 2091);
        data.put("inverterSn4", 64);
        data.put("setFlag", 2);
        data.put("errorCode1", 32);
        data.put("errorCode2", 0);
        data.put("errorCode3", 0);
        data.put("errorCode4", 0);
        data.put("errorCode5", 0);
        data.put("workStatus", 8);
        data.put("systemTimeYear", 20);
        data.put("systemTimeMonth", 1);
        data.put("systemTimeDay", 1);
        data.put("systemTimeHour", 0);
        data.put("systemTimeMinute", 0);
        data.put("systemTimeSecond", 0);
        data.put("leakageProtect", 0);
        data.put("insulationProtect", 0);
        data.put("powerLimitSwitch", 2);
        data.put("reactivePowerSwitch", 1);
        data.put("powerLimitingSwitch", 170);
        data.put("reactiveAdjustmentSwitch", 85);
        data.put("builtInEPMSwitch", 3);
        data.put("builtInEPMReflowPower", 0);
        data.put("builtEPMFailSafeSwitch", 1);
        data.put("EPMRealTimeReflowPower", 0);
        data.put("totalPVV", 7580);
        data.put("totalPVA", 0);
        data.put("pv1A", 0);
        data.put("pv2A", 0);
        data.put("pv3A", 0);
        data.put("pv4A", 0);
        data.put("pv5A", 0);
        data.put("pv6A", 0);
        data.put("pv7A", 0);
        data.put("pv1V", 7654);
        data.put("pv2V", 7586);
        data.put("pv3V", 16);
        data.put("pv4V", 8);
        data.put("pv5V", 0);
        data.put("pv6V", 0);
        data.put("pv7V", 0);
        data.put("inverterStatus", 0);
        data.put("cRC16Check", 0);
        data.put("pidDCBusV", 0);
        data.put("epmACVA", 0);
        data.put("epmACAA", 0);
        data.put("epmACVB", 0);
        data.put("epmACAB", 0);
        data.put("epmACVC", 0);
        data.put("epmACAC", 0);
        data.put("epmAPhasePower", 0);
        data.put("epmBPhasePower", 0);
        data.put("epmCPhasePower", 0);
        data.put("epmTotalPhasePower", 0);
        data.put("inverterTotalPower", 0);
        data.put("inverterModel", 0);
        data.put("inverterSoftwareVersion", 0);
        data.put("powerLimitPercent", 0);
        data.put("ctSensorRatioRatio", 0);
        data.put("returnPowerSet", 0);
        data.put("inverterSetNumber", 0);
        data.put("realTimeClockYear", 0);
        data.put("realTimeClockMonth", 0);
        data.put("realTimeClockDay", 0);
        data.put("realTimeClockHour", 0);
        data.put("realTimeClockMinute", 0);
        data.put("realTimeClockSecond", 0);
        data.put("failSafeSwitch", 0);

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
