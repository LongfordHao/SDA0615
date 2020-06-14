package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.tfjy.sda.bean.CourseExercise;
import com.tfjy.sda.bean.TopicList;
import com.tfjy.sda.mapper.CourseExerciseMapper;
import com.tfjy.sda.mapper.CourseMapper;
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
import tk.mybatis.mapper.entity.Example;

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
    public void getTopicList() throws InterruptedException {
        //调用登录方法
        ChromeDriver driver = courseFeignService.login();

        //查询所有课程中讨论列表的相关html地址
        List<CourseExercise> courseExercises = courseExerciseMapper.selectAll();

        //循环遍历不同课题下的讨论界面中的讨论题目
        for (CourseExercise courseExercise : courseExercises) {

            //获取讨论html地址
            String courseExerciseUrl = courseExercise.getTopicUrl();

            String courseId = courseExercise.getCourseId();
            System.out.println("+++++++++" + courseExerciseUrl + "+++++++++++");
            //打开HTML地址
            driver.get(courseExerciseUrl);
            //页面显示有隐藏的内容，显示隐藏内容
            //点击查看更多

            //*[@id="getMoreTopic"]
            By allMore = new By.ByXPath("//*[@id=\"getMoreTopic\"]");
            //判断是否有查看更多按钮
            if (ElementUtil.check(driver, allMore)) {
                int j = 1;
                boolean b = false;
                for (int i = 0; i < j; i++) {
                    WebElement element = driver.findElement(By.xpath("/html"));
                    String html = element.getAttribute("outerHTML");
                    Document document = Jsoup.parse(html);
                    Elements clearfix = document.select("a[style=display : none]");
                    if (b == false) {
                        WebElement allMoreclick = driver.findElement(By.id("getMoreTopic"));
                        //点击按钮
                        allMoreclick.click();
                        //下拉到页面底部
                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
                        element = driver.findElement(By.xpath("/html"));
                        Thread.sleep(4000);
                        html = element.getAttribute("outerHTML");

                        document = Jsoup.parse(html);
                        Elements topic = document.select("#getMoreTopic");
                        b = topic.attr("style").contains("display: none;");
                        if (b == false) {
                            j += 1;
                        }

                    } else {
                        break;
                    }

                }
            } else {
                //下拉到页面底部
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }
            //获取整个页面topic的信息
            WebElement elementById = driver.findElement(By.xpath("//div[contains(@id,'showTopics')]"));
            String topicReplys = elementById.getAttribute("innerHTML");
            Document parse = Jsoup.parse(topicReplys);
            //获取所有讨论的DIV，除去置顶的元素
            Elements allDiscuss = parse.select(".oneDiv");
            //判断有没有讨论的内容
            if (allDiscuss.size() != 0) {
                for (Element discuss : allDiscuss) {

                    String topicSource = "0";   //讨论的来源如果不显示来源则默认为0
                    String topicReplyCount = "0";//讨论的回复数量。没有回复默认为0
                    Elements title = parse.select(".oneTop"); //页面布局中姓名和时间在头部显示
                    //讨论主题发起者
                    String initiator = title.select(".name").first().text();
                    //讨论主题发起时间danwei
                    String time = title.select(".danwei").first().text();
                    //讨论标题
                    String questionerTitle = discuss.select("h3").first().text();
                    //获取详情讨论的链接地址
                    Elements t = discuss.select("div[onclick]");
                    String onclick = t.get(0).attributes().get("onclick");
                    String[] b = onclick.split("'");
                    String topicUrl = "https://mooc1-1.chaoxing.com" + b[1];

                    Elements source = discuss.select("a");  //查找课程来源
                    for (Element comment : source) {
                        String sourceStyle = comment.attr("style");
                        String replyCount = comment.attr("class");
                        if (sourceStyle.equals("color: #fff;")) {
                            topicSource = comment.text().substring(5);
                        } else if (replyCount.equals("lookall")) {
                            //获取评论数
                            topicReplyCount = comment.text().substring(5);
                        }
                    }


                    //插入数据库中
                    Example example = new Example(TopicList.class);
                    example.createCriteria().andEqualTo("topicTime", time).andEqualTo("topicName", questionerTitle);
                    // example.createCriteria().andEqualTo("topicName",questionerTitle);
                    List<TopicList> exercise = topicListMapper.selectByExample(example);
                    //上面的三条语句相当于下面的查询语句
                    // slelect count(*) from topic_list where topic_name=questionerTitle and topic_time=time
                    TopicList topicList = new TopicList();
                    topicList.setId(IdUtil.randomUUID());
                    topicList.setTopicName(questionerTitle);
                    topicList.setTopicContent(initiator);
                    topicList.setTopicTime(time);
                    topicList.setTopicUrl(topicUrl);
                    topicList.setCourseId(courseId);
                    topicList.setTopicSource(topicSource);
                    topicList.setTopicReplyCount(topicReplyCount);
                    //先查找评论存不存在存在则更新评论不存在则插入评论
                    if (exercise.size()>0 && exercise!=null) {
                        String id = null;
                        for (TopicList topiclist : exercise) {
                            id = topiclist.getId();

                        }
                        topicList.setId(id);
                        topicListMapper.updateByPrimaryKey(topicList);


                    } else {
                        //更新语句
                        topicList.setId(IdUtil.randomUUID());
                        topicListMapper.insert(topicList);

                    }


                }
            }
        }
    }
}


