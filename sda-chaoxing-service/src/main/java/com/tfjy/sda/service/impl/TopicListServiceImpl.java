package com.tfjy.sda.service.impl;

import com.tfjy.sda.CourseExercise;
import com.tfjy.sda.mapper.CourseExerciseMapper;
import com.tfjy.sda.mapper.TopicListMapper;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.service.TopicListService;
import com.tfjy.sda.util.ElementUtil;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/5
 * Time: 9:29
 * Description:
 */
@Service
public class TopicListServiceImpl implements TopicListService {

    @Autowired
    private TopicListMapper topicListMapper;
    @Autowired
    private CourseFeignService courseFeignService;
    @Autowired
    private CourseExerciseMapper courseExerciseMapper;
    @Override
    public void getTopicList(){
        //调用登录方法
        ChromeDriver driver=courseFeignService.login();
        //查询所有课程中讨论列表的相关html地址
        List<CourseExercise> courseExercises=courseExerciseMapper.selectAll();
        //循环遍历不同课题下的讨论界面中的讨论题目
        for (CourseExercise courseExercise:courseExercises){

            //获取讨论html地址
            String courseExerciseUrl=courseExercise.getTopicUrl();
            System.out.println("+++++++++"+courseExerciseUrl+"+++++++++++");
            //打开HTML地址
            driver.get(courseExerciseUrl);
            //页面显示有隐藏的内容，显示隐藏内容
            //点击查看更多

            //*[@id="getMoreTopic"]
            By allMore=new By.ByXPath("//*[@id=\"getMoreTopic\"]");
            //判断是否有查看更多按钮
            if (ElementUtil.check(driver,allMore)){
                WebElement allMoreclick = driver.findElement(By.id("getMoreTopic"));
                //点击按钮
                allMoreclick.click();
                //下拉到页面底部
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }else {
                //下拉到页面底部
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }

            WebElement element = driver.findElement(By.xpath("/html"));
            String html = element.getAttribute("outerHTML");
            Document document = Jsoup.parse(html);

            WebElement elementById = element.findElement(By.xpath("//div[contains(@id,'showTopics')]"));
            String topicReplys =  elementById.getAttribute("innerHTML");
            Document parse = Jsoup.parse(topicReplys);
            Elements clearfix = parse.select("div");
            //System.out.println(clearfix);
            //讨论标题集合，获取页面中讨论列表的topic_id
            List<String> commentList = new ArrayList<String>();
            for (Element comment : clearfix) {
                if (comment.attr("id").contains("topic_")){
                    //将讨论id放入到讨论数组中
                    commentList.add(comment.attr("id").replace("topic_",""));
                }
            }
            //提取讨论相关数据
            for (int j = 0; j < commentList.size(); j++) {
                //获取讨论主题
                Elements topic = document.select("#topic_"+commentList.get(j));

                //讨论主题发起者
                String initiator = topic.select(".name").first().text();
                System.out.println("老师姓名"+initiator);
                //讨论主题发起时间danwei
                String time = topic.select(".danwei").first().text();
                System.out.println("创建时间"+time);
                //讨论标题
                String questionerTitle = topic.select("h3").text();
                System.out.println("讨论内容"+questionerTitle);
                Elements t=topic.select("div[onclick]");
                String a= t.get(0).attributes().get("onclick");
                String[] b=a.split("'");
                //String b=a.substring(11, a.indexOf("'"));
                String topicUrl="https://mooc1-1.chaoxing.com/"+b[1];

                //String onclick=topicUrl.attr("onclick");


                System.out.println("讨论地址"+topicUrl);
            }
        }
    }
}
