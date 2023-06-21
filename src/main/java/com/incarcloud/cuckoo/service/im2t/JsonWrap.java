package com.incarcloud.cuckoo.service.im2t;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.incarcloud.cuckoo.service.IDev;

import java.time.Instant;
import java.util.Base64;

public class JsonWrap implements IDev {
    private final static Base64.Encoder c_encoder = Base64.getEncoder();
    private String fileName;
    private Instant timestamp;
    private String messageId;
    private String vehicleModel;

    public JsonWrap(){
        timestamp = Instant.now();
        fileName = "LSJE36096MS140495_20230519_111509_E_V2.0.6.8.bl.gz";
        messageId = "LSJE36096MS1404951684466109643";
        vehicleModel  = "ep33l";
    }

    public JsonNode toJsonNode(byte[] data){
        ObjectMapper mapper = Im2tJsonMapper.getMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("fileName", fileName);
        root.put("fileContent", c_encoder.encodeToString(data));
        root.put("timestamp", timestamp.toEpochMilli());
        root.put("messageId", messageId);

        ObjectNode nodeExt = mapper.createObjectNode();
        nodeExt.put("vehicleModel", vehicleModel);
        root.set("ext", nodeExt);

        return root;
    }

    @Override
    public byte[] makeDataPackage() {
        try {
            String sample = "Hello";
            byte[] data = sample.getBytes();
            JsonNode root = this.toJsonNode(data);
            return Im2tJsonMapper.getMapper().writeValueAsBytes(root);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
