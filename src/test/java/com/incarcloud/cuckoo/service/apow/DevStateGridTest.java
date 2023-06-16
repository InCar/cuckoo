package com.incarcloud.cuckoo.service.apow;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DevStateGridTest {
    private static final Logger s_logger = LoggerFactory.getLogger(DevStateGridTest.class);
    @Test
    public void testForJsonString(){
        DevStateGrid devStateGrid = new DevStateGrid("Grid001", "1", "StateGrid");
        devStateGrid.setAcquisitionTime(java.time.Instant.now());
        String strJson = new String(devStateGrid.makeDataPackage());
        s_logger.info(strJson);
    }
}
