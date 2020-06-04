package com.tfjy.sda.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @ClassName: FeignConfig
 * @description // TODO  日志打印配置
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/2 9:09
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

}
