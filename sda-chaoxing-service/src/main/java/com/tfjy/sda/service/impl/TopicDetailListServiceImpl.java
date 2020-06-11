package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.bean.TopicList;
import com.tfjy.sda.bean.TopicQuestion;
import com.tfjy.sda.bean.TopicReply;
import com.tfjy.sda.mapper.TopicDetailListMapper;
import com.tfjy.sda.mapper.TopicListMapper;
import com.tfjy.sda.mapper.TopicQuestionMapper;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.service.TopicDetailListService;
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
 * @description
 * @Version:V1.0
 * @params
 * @return
 * @auther: 刘一鸣
 * @date: 2020-06-07 11:34
 */
@Service
public class TopicDetailListServiceImpl implements TopicDetailListService {

    @Autowired//完成自动装配工作，让spring完成bean自动装配的工作。在 Spring 中，构成应用程序主干并由Spring IoC容器管理的对象称为bean。
    private TopicListMapper topicListMapper;
    @Autowired//@Autowired 可以对成员变量、方法、构造函数进行标注。
    private TopicDetailListMapper topicDetailListMapper;
    @Autowired
    private TopicQuestionMapper topicQuestionMapper;
    @Autowired
    private CourseFeignService courseFeignService;

    @Override
    public void getDetailList() throws InterruptedException {
        //调用登录方法
        ChromeDriver driver = courseFeignService.login();
        //查询所有课程中讨论详情的相关html地址
        List<TopicList> topicLists = topicListMapper.selectAll();
        //循环遍历问题内容
        for ( TopicList topicList:topicLists) {
            //获取讨论详情的html地址
            String topicListDetail = topicList.getTopicUrl();
            //获取讨论的话题
            String topicId = topicList.getId();
            System.out.println("###############################" + topicListDetail);
            //打开html地址
            driver.get(topicListDetail);
            //点击查看更多
            By allMore = new By.ByXPath("//*[@id=\"more_reply\"]");
            //判断是否有查看更多按钮
            if (ElementUtil.check(driver, allMore)) {
                int j = 1;
                for (int i = 0; i < j; i++) {
                    if (ElementUtil.check(driver, allMore)) {
                        WebElement allMoreclick = driver.findElement(By.id("more_reply"));
                        //点击查看更多按钮
                        allMoreclick.click();
                        //下拉页面到底部
                        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
                        allMore = new By.ByXPath("//*[@id=\"more_reply\"]");//xpath用来定位
                    } else {
                        break;
                    }
                }
            } else {
                //下拉到页面底部
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
            }
            WebElement element = driver.findElement(By.xpath("/html"));
            String html = element.getAttribute("outerHTML");
            Document document = Jsoup.parse(html);
            //回复集合
            List<String> replyList = new ArrayList<String>();
            //讨论集合
            List<String> commentList = new ArrayList<String>();
            //截取url中的topicid
            String[] b=topicListDetail.split("&");
            String[]c= b[2].split("=");
            Elements Comment = document.select("#topic_replys_"+c[1]).select("div[id]");
            for (Element comment : Comment) {
                if (comment.attr("id").contains("plDiv_")) {
                    //将讨论id放入到讨论数组中
                    commentList.add(comment.attr("id").replace("plDiv_", ""));
                }
            }
            System.out.println("讨论数组长度" + commentList.size() + commentList);
            for (int j = 0; j < commentList.size(); j++) {
                //获取评论主题
                Elements topicReplys = document.select("#plDiv_" + commentList.get(j));
//                System.out.println("查看发起讨论ID：" + commentList.get(j));
                Elements select = topicReplys.select("#second_data_" + commentList.get(j)).select("div[id]");
                for (Element e : select) {
                    if (e.attr("id").contains("second_reply")) {
                        //截取回复人id
                        //将回复id放入到回复数组中
                        replyList.add(e.attr("id").replace("second_reply_", ""));
                    }
                }
//                System.out.println(replyList + "大小：" + replyList.size());
                //讨论发起者
                String initiator = topicReplys.select(".name").first().text();
//                System.out.println("发起讨论者：" + initiator);
                //讨论标题
                Elements topicTitle = topicReplys.select("#replyfirstname_" + commentList.get(j));
                Elements h3 = topicReplys.select("h3");
                String className = topicReplys.select(".fl").select(".photo18").attr("title");
//                System.out.println(className);
//                System.out.println("讨论标题：" + h3.text());
                //将讨论标题作为问题存入到topic_question库中，并去重

                String id=IdUtil.randomUUID();//插入的问题主键id
                TopicQuestion topicQuestion=new TopicQuestion();
                Example example=new Example(TopicQuestion.class);
                example.createCriteria().andEqualTo("questionContent",h3.text());
                int exercise=topicQuestionMapper.selectCountByExample(example);
                if (exercise !=0){
                    System.out.println("该条数据已存在"+j);
                }else {


                    topicQuestion.setId(id);
                    topicQuestion .setQuestionContent(h3.text());
//                topicQuestion.setQuestionTime();
                    topicQuestion.setQuestioner(initiator);
//                topicQuestion.setThumbsUP();
                    topicQuestion.setTopicId(topicId);
//                topicQuestion.setScore();
                    topicQuestionMapper.insert(topicQuestion);
                }



//**************************讨论回复相关************************
                String questionId = commentList.get(j);
                //遍历回复数组的id查找回复内容
                for (int i = 0; i < replyList.size(); i++) {
                    //获取回复内容：李梦涵 回复 周占颖：ok,问题已经顺利解决了，谢谢热心帮助了。04-24 11:31
                    //获取回复中的div
                    Elements selectTopicRe = topicReplys.select("div.width680");
                    String replyPeoples = selectTopicRe.select("#reply_div_" + replyList.get(i)).select(".tiOne").text();
//                    System.out.println(replyPeoples);
                    if (replyPeoples.equals("")==false){
                        //截取回复人姓名
                        String replyPeople = replyPeoples.substring(replyPeoples.indexOf(""), replyPeoples.indexOf(" 回复"));
//                        System.out.println("回复人：" + replyPeople + "  长度：" + replyPeople.length());
                        //截取被回复人姓名
                        String respondent = replyPeoples.substring(replyPeoples.indexOf("回复 ") + 3, replyPeoples.indexOf("：")).trim();
//                        System.out.println("被回复人：" + respondent + "  长度：" + respondent.length());
                        //截取回复消息
                        String replyContent = replyPeoples.substring(replyPeoples.indexOf("：") + 1, replyPeoples.indexOf(getLastString(replyPeoples, 11)));
//                        System.out.println("回复消息 :" + replyContent);
                        //截取回复日期
//                        System.out.println("回复日期：" + getLastString(replyPeoples, 11));

                        //去重并存表
                        Example exampleRe=new Example(TopicReply.class);
                        String time=getLastString(replyPeoples, 11);
                        exampleRe.createCriteria().andEqualTo("replyTim",time);
                        exampleRe.createCriteria().andEqualTo("replyContent",replyContent);
                        int exerciseRe=topicDetailListMapper.selectCountByExample(exampleRe);
                        if (exerciseRe !=0){
                            System.out.println("该条数据已存在"+i);
                        }else {

                            TopicReply topicReply=new TopicReply();
                            topicReply.setId(IdUtil.randomUUID());
                            topicReply.setQuestionId(topicQuestion.getId());
                            topicReply.setReplied(respondent);
                            topicReply.setReplyName(replyPeople);
                            topicReply.setReplyContent(replyContent);
                            topicReply.setReplyTim(time);
                            topicDetailListMapper.insert(topicReply);
                        }
                    }

                }
            }

        }

    }
    private  static  String getLastString(String source,int lenth){
        return source.length()>=lenth?source.substring((source.length()-lenth)):source;
    }
}
