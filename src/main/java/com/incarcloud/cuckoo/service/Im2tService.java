package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.service.im2t.SimSimpleIm2t;
import org.springframework.stereotype.Service;

// 智己(IMMOTORS)
@Service
public class Im2tService {
    private ISim simOnRunning;

    public boolean isRunning(){
        return simOnRunning != null;
    }

    public void start(Im2tSimArgs args){
        if(simOnRunning != null) return;

        SimSimpleIm2t ss = new SimSimpleIm2t(args);
        ss.start();
        this.simOnRunning = ss;
    }

    public void stop(){
        if(simOnRunning == null) return;

        this.simOnRunning.stop();
        this.simOnRunning = null;
    }
}
