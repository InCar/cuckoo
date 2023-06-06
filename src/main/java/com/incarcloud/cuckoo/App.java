package com.incarcloud.cuckoo;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    private static final Logger s_logger = org.slf4j.LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GitVer version = new GitVer();
        s_logger.info("AppVer: {}", version.getVersion());

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            s_logger.info("Quit.");
        }));
    }
}
