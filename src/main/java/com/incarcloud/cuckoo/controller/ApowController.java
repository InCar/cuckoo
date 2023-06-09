package com.incarcloud.cuckoo.controller;

import com.incarcloud.cuckoo.service.ApowSimArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cuckoo.target.host}")
    private String host;

    @Value("${cuckoo.target.port}")
    private int port;

    @Value("${cuckoo.target.topic}")
    private String topic;

    @GetMapping("/state")
    public ResponseEntity<?> getState() {
        RunningState state = new RunningState();
        state.isRunning = apowService.isRunning();
        return ResponseEntity.ok(state);
    }

    @PutMapping("/start")
    public ResponseEntity<?> start() {
        var args = new ApowSimArgs();
        args.host = host;
        args.port = port;
        args.topic = topic;

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