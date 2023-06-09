package com.incarcloud.cuckoo.service.apow;

import com.incarcloud.cuckoo.cape.mq.mqtt.PahoV5;
import com.incarcloud.cuckoo.service.ApowSimArgs;
import com.incarcloud.cuckoo.service.ISim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

// 以固定速率发送同样的数据
public class SimpleSim implements ISim {
    private final static Logger s_logger = LoggerFactory.getLogger(SimpleSim.class);
    private final float MAX_SPEED = 100.0f; // 每秒发送的数据包数
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

        try(var pahoV5 = new PahoV5(this.taskArgs.host, this.taskArgs.port, "PahoV5")){
            pahoV5.connect("cuckooTest");

            // 用于测量发送速度
            long markBegin = System.nanoTime();
            int countBegin = count;

            while(!atomCanStop.get()){
                count++;
                if(count < 0) count = 0; // 避免长时间运行后溢出

                pahoV5.sendAsync(this.taskArgs.topic, "Hello");

                if(count % 100 == 0){
                    float speed = MAX_SPEED;
                    long markNow = 0;
                    while(speed >= MAX_SPEED){
                        markNow = System.nanoTime();
                        if(markNow <= markBegin) markNow = markBegin + 1; // 防止除0
                        speed = (count-countBegin) * 1.0e9f / (markNow - markBegin);
                        if(speed >= MAX_SPEED) {
                            float t = 1000.0f * 100.0f/MAX_SPEED;
                            float sleepX = t - (markNow - markBegin)/1.0e6f;
                            Thread.sleep((int)sleepX);
                        }
                    }

                    countBegin = count;
                    markBegin = markNow;
                }
            }

            pahoV5.disconnect();
        }
        catch (Exception e){
            s_logger.error("SimpleSim.run", e);
        }
    }
}
