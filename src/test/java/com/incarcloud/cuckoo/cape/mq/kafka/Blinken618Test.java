package com.incarcloud.cuckoo.cape.mq.kafka;

import com.incarcloud.cuckoo.dto.KafkaArgs;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

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

            int count = 100;
            AtomicInteger atomicCount = new AtomicInteger(0);

            for (int i = 0; i < count; i++) {
                blinken618.sendAsync(kafkaArgs.getTopic(), "Hello, Kafka!".getBytes());
            }

            blinken618.recvAsync(kafkaArgs.getTopic(), (bufRecv) -> {
                String msg = new String(bufRecv);
                s_logger.info("recv: {}", msg);
                if(atomicCount.incrementAndGet() >= count){
                    blinken618.stopRecv();
                }
            });
        }catch(Exception ex){
            throw ex;
        }
    }
}
