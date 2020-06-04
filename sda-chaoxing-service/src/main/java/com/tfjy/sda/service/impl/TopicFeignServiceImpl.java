package com.tfjy.sda.service.impl;
import com.google.common.collect.Lists;

/*
 * @ClassName: TopicServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:04
 */

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.tfjy.sda.Topic;
import com.tfjy.sda.mapper.TopicMapper;
import com.tfjy.sda.service.TopicFeignService;
import com.tfjy.sda.util.PropertiesUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TopicFeignServiceImpl implements TopicFeignService {
    @Autowired
    private TopicMapper topicMapper;
    @Override
    @HystrixCommand(fallbackMethod = "timeOutHandler",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "3000")
            //三秒以内，服务正常。超时设置
    })
    public List<Topic> queryAll() {
//        try{
//            TimeUnit.MILLISECONDS.sleep(Long.parseLong("5000"));
//        }
//        catch (InterruptedException e){
//            e.printStackTrace();
//        }

        return topicMapper.selectAll();
    }

    @Override
    public String queryTest() {
        //String s = topicMapper.selectAll().toString();
        return "成功了，O(∩_∩)O ";
    }
    /*
     * @Description //TODO 学习通登录方法
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/2 15:39
     */

    @Override
    public ChromeDriver login() {
        //初始化浏览器
        ChromeDriver chromeDriver = new ChromeDriver();
        //打开网页
        chromeDriver.get("https://passport2.chaoxing.com/login?fid=&newversion=true&refer=http%3A%2F%2Fi.chaoxing.com");

        try {
            //输入账号
            WebElement phone = chromeDriver.findElement(By.id("phone"));
            String usrname = PropertiesUtil.getValue("chaoxing.uname");
            phone.sendKeys(usrname);
            Thread.sleep(2000);
            //输入密码
            WebElement pwd = chromeDriver.findElement(By.id("pwd"));
            String password = PropertiesUtil.getValue("chaoxing.password");
            pwd.sendKeys(password);
            Thread.sleep(2000);
            //点击登录
            WebElement loginBtn = chromeDriver.findElement(By.id("loginBtn"));
            loginBtn.click();
            Thread.sleep(2000);
           return chromeDriver;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chromeDriver;
    }


    /*
     * @Description //TODO  登录老师端首页
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/4 14:49
     */

    @Override
    public void homePage(){
        ChromeDriver driver = login();
        String curriculumUrl = PropertiesUtil.getValue("curriculum.url");
        //进入首页
        driver.get(curriculumUrl);
        //获取首页，课程信息
        WebElement element = driver.findElement(By.xpath("/html"));
        String html = element.getAttribute("outerHTML");
        Document curriculum = Jsoup.parse(html);
        Elements ulDiv = curriculum.select(".ulDiv");
        System.out.println("ulDiv"+ulDiv);
        //获取ul标签内容
        Elements ulelement = curriculum.getElementsByTag("ul");
        //
        for (Element element1 : ulelement) {

        }
    }

    public List<Topic> timeOutHandler(){
        List<Topic> topiclist=Lists.newArrayList();
        return topiclist;
    }
    //========================服务熔断
    @HystrixCommand(fallbackMethod = "CiruitBreakerFallback",commandProperties = {
            @HystrixProperty(name="circuitBreaker.enable",value = "true"),//是否开启断路器
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "10"),//请求次数
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//时间窗口期
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value = "60"),//失败率到达多少跳闸
    })
    public String CiruitBreaker(){
        return  Thread.currentThread().getName();
    }
    public String CiruitBreakerFallback(){
        return "服务熔断出错，请稍后重试";
    }
}
