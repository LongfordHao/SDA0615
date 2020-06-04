package com.tfjy.sda.controller;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tfjy.sda.Topic;
import com.tfjy.sda.service.TopicFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


/*
 * @ClassName: ChaoxingController
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 14:43
 */
@RestController
@RequestMapping("/consume")
@Slf4j
@DefaultProperties(defaultFallback = "fallbackMethod")
public class ChaoxingController {
    @Resource
    private TopicFeignService topicFeignService;
    @RequestMapping("/all")
    public List<Topic> queryAll(){
        return this.topicFeignService.queryAll();
    }
    @HystrixCommand
    @RequestMapping("/test")
    public String queryTest(){
        int a=5/0;
        return this.topicFeignService.queryTest();
    }

    public String fallbackMethod(){
        return "接口异常，请稍后重试，/(ㄒoㄒ)/~~";
    }
}
