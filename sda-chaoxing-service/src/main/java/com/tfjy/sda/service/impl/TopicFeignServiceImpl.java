package com.tfjy.sda.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.tfjy.sda.AddIntegralModel;
import com.tfjy.sda.Course;
import com.tfjy.sda.Topic;
import com.tfjy.sda.mapper.TopicMapper;
import com.tfjy.sda.service.TopicFeignService;
import com.tfjy.sda.util.PropertiesUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TopicFeignServiceImpl implements TopicFeignService {
    @Value("${jurisdiction.loginUrl}")
    private String loginUrl;

    @Value("${integral.addUrl}")
    private String addUrl;

    private TopicMapper topicMapper;
//    @Autowired
//@Autowired
//    private JurisdictionFeignService jurisdictionFeignService;

    @Override
    @HystrixCommand(fallbackMethod = "timeOutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            //三秒以内，服务正常。超时设置
    })
    public List<Topic> queryAll() {
        return topicMapper.selectAll();
    }

    @Override
    public String queryTest() {
        AddIntegralModel addIntegralModel = new AddIntegralModel();
        addIntegralModel.setIntegral("10");
        addIntegralModel.setReason("无理由加分");
        addIntegralModel.setUserId("13483607023");
        addIntegral(addIntegralModel);
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
    public void homePage() {
        ChromeDriver driver = login();
        String curriculumUrl = PropertiesUtil.getValue("curriculum.url");
        //进入首页
        driver.get(curriculumUrl);
        //获取首页，课程信息
        WebElement element = driver.findElement(By.xpath("/html"));
        String html = element.getAttribute("outerHTML");
        Document curriculum = Jsoup.parse(html);
        Elements ulDiv = curriculum.select(".ulDiv");
        //获取ul标签内容
        Elements ulelement = curriculum.getElementsByTag("ul");
        for (Element elementul : ulelement) {
            //获取li标签内容
            Elements lielement = elementul.getElementsByTag("li");
            for (Element elementli : lielement) {
                //判断是否为课程内容
                if (elementli.attr("style").equals("position:relative")) {
                    //课程名称
                    String courseName = elementli.select("h3").select("a").attr("title");
                    System.out.println(courseName);
                    //课程ulr
                    String courseUrl = elementli.select("h3").select("a").attr("abs:href");
                    System.out.println(courseUrl);
                    //任课老师
                    String courseTeacher = elementli.select("p").first().text();
                    System.out.println(courseTeacher);
                    //课程备注
                    String exlain = elementli.select("p").select("p[^title]").text();
                    System.out.println(exlain);

                    //查询数据库是否存在该课程
                    Course course = new Course();
                    course.setCourseName(courseName);

                }

            }
        }
        driver.get("https://mooc1-1.chaoxing.com/course/isNewCourse?courseId=207434064&edit=true&enc=0a8a8cd32f495e43d9d3429e4d3d3336&v=0");
    }
    /*
     * @Description TODO 加分接口
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/11 8:35
     */

    public void addIntegral(AddIntegralModel addIntegral) {
        //构建权限登录接口参数
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("password", addIntegral.getUserId());
        hashMap.put("userCode", addIntegral.getUserId());
        String result = HttpRequest.post(loginUrl)
                .body(JSON.toJSONString(hashMap))
                .execute().body();
        JSONObject jsonObject = JSONObject.parseObject(result);
        //获取token与id
        String token = jsonObject.getJSONObject("data").getString("token");
        String id = jsonObject.getJSONObject("data").getString("id");
        System.out.println(token);
        System.out.println(id);
        //构建积分加分接口参数
        List<AddIntegralModel> addIntegralModels= new ArrayList<>();
        addIntegral.setUserId(id);
        addIntegralModels.add(addIntegral);

        String addresult = HttpRequest.post(addUrl)
                .body(JSONObject.toJSONString(addIntegralModels))
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .execute().body();
        System.out.println(addresult);

    }

    public List<Topic> timeOutHandler() {
        List<Topic> topiclist = Lists.newArrayList();
        return topiclist;
    }

    //========================服务熔断
    @HystrixCommand(fallbackMethod = "CiruitBreakerFallback", commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enable", value = "true"),//是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//时间窗口期
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),//失败率到达多少跳闸
    })
    public String CiruitBreaker() {
        return Thread.currentThread().getName();
    }

    public String CiruitBreakerFallback() {
        return "服务熔断出错，请稍后重试";
    }
}
