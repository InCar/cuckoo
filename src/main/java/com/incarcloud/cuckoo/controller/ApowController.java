package com.incarcloud.cuckoo.controller;

import com.incarcloud.cuckoo.dto.MqttArgs;
import com.incarcloud.cuckoo.dto.RunningState;
import com.incarcloud.cuckoo.service.ApowSimArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.incarcloud.cuckoo.service.ApowService;

@RestController
@RequestMapping("/api/apow")
public class ApowController {

    @Autowired
    private ApowService apowService;

    @Autowired
    private MqttArgs mqttArgs;

    @GetMapping("/state")
    public ResponseEntity<?> getState() {
        RunningState state = new RunningState();
        state.isRunning = apowService.isRunning();
        return ResponseEntity.ok(state);
    }

    @PutMapping("/start")
    public ResponseEntity<?> start(@RequestBody ApowRequest request){
        var args = new ApowSimArgs(this.mqttArgs, request.speed);

        apowService.start(args);
        return this.getState();
    }

    @PutMapping("/stop")
    public ResponseEntity<?> stop() {
        apowService.stop();
        return this.getState();
    }
}