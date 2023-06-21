package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.service.ISim;
import com.incarcloud.cuckoo.service.Im2tSimArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimSimpleIm2t implements ISim {
    private final static Logger s_logger = LoggerFactory.getLogger(SimSimpleIm2t.class);

    private Im2tSimArgs taskArgs;

    private AtomicBoolean atomCanStop = new AtomicBoolean(false);
    private Thread threadWorking = null;

    public SimSimpleIm2t(Im2tSimArgs args){
        this.taskArgs = args;
    }

    @Override
    public void start() {
        // 如果已经在运行中,则无需再次启动
        if(threadWorking != null) return;

        atomCanStop.set(false);
        threadWorking = new Thread(this::run);
        threadWorking.start();
        s_logger.info("SimSimpleIm2t.start");
    }

    @Override
    public void stop() {
        atomCanStop.set(true);
        if(threadWorking != null){
            try {
                threadWorking.join();
            } catch (InterruptedException e) {
                s_logger.error("SimSimpleIm2t.stop", e);
            }
            threadWorking = null;
        }
        s_logger.info("SimSimpleIm2t.stop");
    }

    private void run(){
        int count = 0;

        try {
            while (!atomCanStop.get()) {
                count++;
                s_logger.info("SimSimpleIm2t.run count={}", count);
                Thread.sleep(1000);
            }
        }
        catch (Exception e){
            s_logger.error("SimSimpleIm2t.run", e);
        }
    }
}
