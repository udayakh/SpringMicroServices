package com.appdeveloper.springboot.config.server.photoappspringbootconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PhotoappSpringbootConfigserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhotoappSpringbootConfigserverApplication.class, args);
    }

}
