package com.tfjy.sda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BiliServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BiliServiceApplication.class,args);
    }
}
