package com.incarcloud.cuckoo.cape.mq.mqtt;

import org.eclipse.paho.mqttv5.common.MqttException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PahoV5Test {
    private final static Logger s_logger = LoggerFactory.getLogger(PahoV5Test.class);

    @Value("${cuckoo.test.PahoV5Test.host}")
    private String host;

    @Value("${cuckoo.test.PahoV5Test.port}")
    private int port;

    @Value("${cuckoo.test.PahoV5Test.topic}")
    private String topic;

    @Test
    public void simpleSendAndRecv() throws Exception {

        AtomicInteger atomFly = new AtomicInteger(0);

        try(var pahoV5 = new PahoV5(this.host, this.port, "PahoV5")){
            s_logger.info("host: {}, port: {}", this.host, this.port);
            pahoV5.connect("cuckooTest");
            var ref = new Object() {
                String msgRecved = null;
            };
            int count = 1000000;
            var latch = new CountDownLatch(count);

            pahoV5.recvAsync(this.topic, (msg) -> {
                atomFly.decrementAndGet();
                //s_logger.info("recv msg: {}", msg);
                ref.msgRecved = msg;
                latch.countDown();
            });
            String msgSend = "hello world";
            for(int i=0; i<count; i++) {
                s_logger.info("send msg: {} {}", i, msgSend);
                while(atomFly.get() > 3000){
                    Thread.sleep(1);
                    s_logger.info("message fly: {}", atomFly.get());
                }
                pahoV5.sendAsync(this.topic, msgSend);
                atomFly.incrementAndGet();
            }

            latch.await();
            pahoV5.disconnect();
            Assertions.assertEquals(msgSend, ref.msgRecved);
        }
        catch (Exception e){
            s_logger.error("message fly: {}", atomFly.get());
            throw e;
        }
    }
}
