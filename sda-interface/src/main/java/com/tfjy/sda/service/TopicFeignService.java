package com.tfjy.sda.service;


import com.tfjy.sda.Topic;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.Document;
import java.util.List;

/*
 * @ClassName: TopicService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 14:33
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface TopicFeignService {
     //查询讨论表所有信息
     @RequestMapping ("/all")
     List<Topic> queryAll();
     @RequestMapping ("/test")
     String queryTest();
     //学习通登录方法
     ChromeDriver login();
     //进入首页，获取课程信息
     void homePage();
}
