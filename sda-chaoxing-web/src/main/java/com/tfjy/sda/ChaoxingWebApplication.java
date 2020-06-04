package com.tfjy.sda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableHystrixDashboard //服务监控
@EnableFeignClients(basePackages = "com.tfjy.sda.service")
public class ChaoxingWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChaoxingWebApplication.class,args);
    }
}
