package com.appdeveloper.discovery.server.discoverymainappservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryMainAppServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryMainAppServiceApplication.class, args);
    }

}
