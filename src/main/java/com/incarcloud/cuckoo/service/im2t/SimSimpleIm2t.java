package com.incarcloud.cuckoo.service.im2t;

import com.incarcloud.cuckoo.cape.mq.kafka.Blinken618;
import com.incarcloud.cuckoo.service.ISim;
import com.incarcloud.cuckoo.service.Im2tSimArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

// 这是一种最简单的模拟器,只模拟一台车
public class SimSimpleIm2t implements ISim {
    private final static Logger s_logger = LoggerFactory.getLogger(SimSimpleIm2t.class);

    private Im2tSimArgs taskArgs;

    // 简单模拟一台车
    private final VehicleX vehicleX;

    // Kafka发送器
    private final Blinken618 blinken618;

    private AtomicBoolean atomCanStop = new AtomicBoolean(false);
    private Thread threadWorking = null;

    public SimSimpleIm2t(Im2tSimArgs args){
        this.taskArgs = args;
        // 当前只有这一种车型
        var tm = Instant.now();
        this.vehicleX = new VehicleEP33L("LSJE36096TEST0001", tm);
        this.blinken618 = new Blinken618(args.host, args.port, args.topic);
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
            } catch (Exception e) {
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
                // 这个简单模拟器直接使用了当前时间,但这不是必须的
                Instant tmNow = Instant.now();
                byte[] data = this.vehicleX.makeDataPackage(tmNow);

                // this.blinken618.sendAsync(this.taskArgs.topic, data);

                count++;
                s_logger.info("SimSimpleIm2t.run count={}", count);
                Thread.sleep(1000); // 每秒1次
            }
        }
        catch (Exception e){
            s_logger.error("SimSimpleIm2t.run", e);
        }
    }
}
