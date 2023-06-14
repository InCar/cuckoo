package com.incarcloud.cuckoo.cape.mq.mqtt;

import com.incarcloud.cuckoo.dto.MqttArgs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ComponentScan("com.incarcloud.cuckoo.dto")
public class PahoV5Test {
    private final static Logger s_logger = LoggerFactory.getLogger(PahoV5Test.class);

    @Autowired
    private MqttArgs mqttArgs;

    @Test
    public void simpleSendAndRecv() throws Exception {

        AtomicInteger atomFly = new AtomicInteger(0);

        try(var pahoV5 = new PahoV5(mqttArgs.getHost(), mqttArgs.getPort(), "PahoV5")){
            s_logger.info("host: {}, port: {}", mqttArgs.getHost(), mqttArgs.getPort());
            pahoV5.connect("cuckooTest");
            var ref = new Object() {
                String msgRecved = null;
            };
            int count = 1000000;
            var latch = new CountDownLatch(count);

            pahoV5.recvAsync(mqttArgs.getTopic(), (msg) -> {
                atomFly.decrementAndGet();
                //s_logger.info("recv msg: {}", msg);
                ref.msgRecved = msg;
                latch.countDown();
            });
            String msgSend = "hello world";
            for(int i=0; i<count; i++) {
                s_logger.info("send msg: {} {}", i, msgSend);
                while(atomFly.get() > 1000){
                    Thread.sleep(1);
                    s_logger.info("message fly: {}", atomFly.get());
                }
                pahoV5.sendAsync(mqttArgs.getTopic(), msgSend);
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
