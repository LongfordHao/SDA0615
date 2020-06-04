package com.tfjy.sda.controller;
import com.tfjy.sda.Topic;
import com.tfjy.sda.service.TopicFeignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.swing.text.Document;
import java.util.List;

/*
 * @ClassName: ChaoxingProviderControlle
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:01
 */


@RestController
public class ChaoxingProviderControlle {
    @Resource
    private TopicFeignService topicFeignService;
    @RequestMapping("/all")
    public List<Topic> queryAll(){
       return topicFeignService.queryAll();
    }
    @RequestMapping("/test")
    public String queryTest(){
        return this.topicFeignService.queryTest();
    }
    @RequestMapping("/login")
    public void login(){
           topicFeignService.login();
    }
    @RequestMapping("/home")
    public void homePage(){
        topicFeignService.homePage();
    }
}
