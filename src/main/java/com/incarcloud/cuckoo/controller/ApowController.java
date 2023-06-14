package com.incarcloud.cuckoo.controller;

import com.incarcloud.cuckoo.dto.MqttArgs;
import com.incarcloud.cuckoo.service.ApowSimArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> start() {
        var args = new ApowSimArgs(this.mqttArgs);

        apowService.start(args);
        return this.getState();
    }

    @PutMapping("/stop")
    public ResponseEntity<?> stop() {
        apowService.stop();
        return this.getState();
    }
}

class RunningState{
    public boolean isRunning;
}