package com.incarcloud.cuckoo.service.apow;

import com.incarcloud.cuckoo.service.ApowSimArgs;
import com.incarcloud.cuckoo.service.ISim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

// 以固定速率发送同样的数据
public class SimpleSim implements ISim {
    private final static Logger s_logger = LoggerFactory.getLogger(SimpleSim.class);
    private final float speed = 100.0f; // 每秒发送的数据包数
    private final ApowSimArgs taskArgs;

    private AtomicBoolean atomCanStop = new AtomicBoolean(false);
    private Thread threadWorking = null;

    public SimpleSim(ApowSimArgs args){
        this.taskArgs = args;
    }

    @Override
    public void start() {
        // 如果已经在运行中,则无需再次启动
        if(threadWorking != null) return;

        atomCanStop.set(false);
        threadWorking = new Thread(this::run);
        threadWorking.start();
    }

    @Override
    public void stop() {
        atomCanStop.set(true);
        if(threadWorking != null){
            try {
                threadWorking.join();
            } catch (InterruptedException e) {
                s_logger.error("SimpleSim.stop", e);
            }
            threadWorking = null;
        }
    }

    private void run(){
        int count = 0;
        while(!atomCanStop.get()){
            try {
                count++;
                s_logger.info("SimpleSim.run -> {}", count);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
