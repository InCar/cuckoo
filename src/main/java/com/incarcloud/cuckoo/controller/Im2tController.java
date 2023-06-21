package com.incarcloud.cuckoo.controller;

import com.incarcloud.cuckoo.dto.KafkaArgs;
import com.incarcloud.cuckoo.dto.RunningState;
import com.incarcloud.cuckoo.service.ApowSimArgs;
import com.incarcloud.cuckoo.service.Im2tService;
import com.incarcloud.cuckoo.service.Im2tSimArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
