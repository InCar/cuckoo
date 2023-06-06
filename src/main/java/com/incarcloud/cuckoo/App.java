package com.incarcloud.cuckoo;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class App implements CommandLineRunner {
    private static final Logger s_logger = org.slf4j.LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(App.class, args);
    }

    private Environment env;

    public App(Environment env) {
        this.env = env;
    }

    @Override
    public void run(String... args) throws Exception {
        GitVer version = new GitVer();
        s_logger.info("AppVer: {}", version.getVersion());
        loggingBasicInfo();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            s_logger.info("Quit.");
        }));
    }

    private void loggingBasicInfo() {
        String address = env.getProperty("server.address");
        String port = env.getProperty("server.port");

        if(port != null) {
            s_logger.info("WebServer is listening on \033[0;34mhttp://{}\033[0m\033[0;96m:{}\033[0m",
                    address, port);
        }
    }
}
