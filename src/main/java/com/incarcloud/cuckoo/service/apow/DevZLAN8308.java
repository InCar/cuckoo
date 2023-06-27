package com.incarcloud.cuckoo.service.apow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

// 卓岚ZLAN8308. RS485\modbus RTU\4G模块
public class DevZLAN8308 implements IDev {
    private final String deviceID;
    private final ApowDevTypes type;
    private final String orgId;
    private final String tag;

    private final static DateTimeFormatter c_fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);


    public DevZLAN8308(String deviceID, ApowDevTypes type, String orgId, String tag) {
        this.deviceID = deviceID;
        this.type = type;
        this.orgId = orgId;
        this.tag = tag;
    }

    public JsonNode toJsonNode(Instant tm, JsonNode data){
        ObjectMapper mapper = ApowJsonMapper.getMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("acquisitionTime", c_fmt.format(tm));
        root.put("deviceID", deviceID);
        root.put("type", Integer.toString(type.getValue()));
        root.put("orgId", orgId);
        root.put("tag", tag);
        root.set("data", data);

        return root;
    }

    @Override
    public byte[] makeDataPackage(Instant tm) {
        ObjectNode data = ApowJsonMapper.getMapper().createObjectNode();
        JsonNode root = this.toJsonNode(tm, data);
        try {
            return ApowJsonMapper.getMapper().writeValueAsBytes(root);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
