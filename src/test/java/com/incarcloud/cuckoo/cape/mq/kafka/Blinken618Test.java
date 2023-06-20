package com.incarcloud.cuckoo.cape.mq.kafka;

import com.incarcloud.cuckoo.dto.KafkaArgs;
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
public class Blinken618Test {
    private final Logger s_logger = LoggerFactory.getLogger(Blinken618Test.class);

    @Autowired
    private KafkaArgs kafkaArgs;

    @Test
    void simpleSendAndRecv() throws Exception{
        try(Blinken618 blinken618 = new Blinken618(kafkaArgs.getHost(), kafkaArgs.getPort(), kafkaArgs.getGroupId())) {
            int count = 1000;
            CountDownLatch latch = new CountDownLatch(count);
            // AtomicInteger atomicCount = new AtomicInteger(0);

            blinken618.recvAsync(kafkaArgs.getTopic(), (bufRecv) -> {
                String msg = new String(bufRecv);
                // s_logger.info("recv: {} {}", atomicCount.getAndIncrement(), msg);
                latch.countDown();
            });

            for (int i = 0; i < count; i++) {
                String msg = String.format("Hello, Kafka! %d", i);
                blinken618.sendAsync(kafkaArgs.getTopic(), msg.getBytes());
            }

            latch.await();
            blinken618.stopRecv();

            s_logger.info("done");

        }catch(Exception ex){
            throw ex;
        }
    }
}
