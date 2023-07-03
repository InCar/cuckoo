package com.incarcloud.cuckoo.service.im2t;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RVMBaseSignlTest {
    private static final Logger s_logger = LoggerFactory.getLogger(RVMBaseSignlTest.class);
    @Test
    void testWriteBuf(){
        var target = new RVMBaseSignl();
        byte[] buf = target.toBytesTestOnly();

        int i = 0;
        StringBuilder sbHex = new StringBuilder();
        for(byte b : buf){
            if(i % 16 == 0){
                sbHex.append("\n");
            }

            sbHex.append(String.format("%02X ", b));
            i++;
        }

        s_logger.info("{}", sbHex.toString());
    }
}
