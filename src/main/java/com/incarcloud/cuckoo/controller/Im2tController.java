package com.incarcloud.cuckoo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.incarcloud.cuckoo.dto.KafkaArgs;
import com.incarcloud.cuckoo.dto.RunningState;
import com.incarcloud.cuckoo.service.Im2tService;
import com.incarcloud.cuckoo.service.Im2tSimArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@RestController
@RequestMapping("/api/im2t")
public class Im2tController {
    @Autowired
    private Im2tService im2tService;

    @Autowired
    private KafkaArgs kafkaArgs;

    @GetMapping("/state")
    public ResponseEntity<?> getState() {
        RunningState state = new RunningState();
        state.isRunning = im2tService.isRunning();
        return ResponseEntity.ok(state);
    }

    @PutMapping("/start")
    public ResponseEntity<?> start() {
        var args = new Im2tSimArgs(this.kafkaArgs);
        this.im2tService.start(args);
        return this.getState();
    }

    @PutMapping("/stop")
    public ResponseEntity<?> stop() {
        im2tService.stop();
        return this.getState();
    }

    @GetMapping("/scripts")
    public ResponseEntity<?> getScripts() {
        return ResponseEntity.ok(im2tService.getScripts());
    }

    @PutMapping("/scripts")
    public ResponseEntity<?> setScripts(@RequestBody String scripts) {
        im2tService.setScripts(scripts);
        return ResponseEntity.ok(scripts);
    }

    @PostMapping("/decode")
    public ResponseEntity<?> decode(@RequestBody JsonNode args) {
        // data is a base64 string
        byte[] buf = Base64.getDecoder().decode(args.get("data").asText());
        // then un gzip
        try(GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(buf))){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Extracted byte array
            byte[] extractedBytes = byteArrayOutputStream.toByteArray();
            // To Hex String
            StringBuilder sbHex = new StringBuilder();
            sbHex.append("{ \"hex\": \"");
            for (byte b : extractedBytes) {
                sbHex.append(String.format("%02X", b));
            }
            sbHex.append("\" }");

            return ResponseEntity.ok(sbHex.toString());
        }
        catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
