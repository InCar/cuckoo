package com.incarcloud.cuckoo.service.apow;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DevStateGridTest {
    private static final Logger s_logger = LoggerFactory.getLogger(DevStateGridTest.class);
    @Test
    public void testForJsonString(){
        DevStateGrid devStateGrid = new DevStateGrid("Grid001", "1", "StateGrid");
        Instant tm = Instant.now();
        String strJson = new String(devStateGrid.makeDataPackage(tm));
        s_logger.info(strJson);
    }
}
