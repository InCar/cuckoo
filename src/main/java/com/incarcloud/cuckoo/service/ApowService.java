package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.service.apow.SimSimpleApow;
import org.springframework.stereotype.Service;

@Service
public class ApowService {
    private ISim simOnRunning;

    public boolean isRunning(){
        return simOnRunning != null;
    }

    public void start(ApowSimArgs args){
        if(simOnRunning != null) return;

        SimSimpleApow ss = new SimSimpleApow(args);
        ss.start();
        this.simOnRunning = ss;
    }

    public void stop(){
        if(simOnRunning == null) return;

        this.simOnRunning.stop();
        this.simOnRunning = null;
    }
}
