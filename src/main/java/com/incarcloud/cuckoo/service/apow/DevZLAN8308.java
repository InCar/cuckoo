package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

// 卓岚ZLAN8308. RS485\modbus RTU\4G模块
public class DevZLAN8308 {
    private final String deviceID;
    private final ApowDevTypes type;
    private final String orgId;
    private final String tag;

    private final static DateTimeFormatter c_fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Instant acquisitionTime;


    public DevZLAN8308(String deviceID, ApowDevTypes type, String orgId, String tag) {
        this.deviceID = deviceID;
        this.type = type;
        this.orgId = orgId;
        this.tag = tag;
    }

    public Instant getAcquisitionTime() {
        return acquisitionTime;
    }

    private String getAcquisitionTimeAsString(){
        return c_fmt.format(acquisitionTime);
    }

    public void setAcquisitionTime(Instant acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }

    public JsonNode toJsonNode(JsonNode data){
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("acquisitionTime", getAcquisitionTimeAsString());
        root.put("deviceID", deviceID);
        root.put("type", Integer.toString(type.getValue()));
        root.put("orgId", orgId);
        root.put("tag", tag);
        root.set("data", data);

        return root;
    }
}