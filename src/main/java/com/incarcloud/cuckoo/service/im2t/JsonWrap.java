package com.incarcloud.cuckoo.service.im2t;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class JsonWrap{
    private final static Base64.Encoder c_encB64 = Base64.getEncoder();

    // ${vin}_${tm:yyyyMMdd}_${tm:HHmmss}_E_${version}.bl.gz
    private final static String c_fmtFileName = "%s_%s_%s_E_%s.bl.gz";
    private final static DateTimeFormatter c_fmtYMD = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);
    private final static DateTimeFormatter c_fmtHMS = DateTimeFormatter.ofPattern("HHmmss").withZone(ZoneOffset.UTC);

    // ${vin}${tm:mills}
    private final static String c_fmtMessageId = "%s%d";
    private final String vin; // 17 characters: LSJE36096MS140495
    private String version = "V2.0.6.8";
    private String vehicleModel = "ep33l";

    public JsonWrap(String vin){
        this.vin = vin;
    }

    public JsonNode toJsonNode(Instant tm, byte[] data){
        ObjectMapper mapper = Im2tJsonMapper.getMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("fileName", String.format(c_fmtFileName, vin, c_fmtYMD.format(tm), c_fmtHMS.format(tm), version));
        root.put("fileContent", c_encB64.encodeToString(data));
        root.put("timestamp", tm.toEpochMilli());
        root.put("messageId", String.format(c_fmtMessageId, vin, tm.toEpochMilli()));

        ObjectNode nodeExt = mapper.createObjectNode();
        nodeExt.put("vehicleModel", vehicleModel);
        root.set("ext", nodeExt);

        return root;
    }

    public byte[] toByteArray(Instant tm, byte[] data) {
        try {
            JsonNode root = this.toJsonNode(tm, data);
            return Im2tJsonMapper.getMapper().writeValueAsBytes(root);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
