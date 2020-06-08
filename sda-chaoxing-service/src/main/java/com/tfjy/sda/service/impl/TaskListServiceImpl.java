package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.CourseExercise;
import com.tfjy.sda.TaskList;
import com.tfjy.sda.mapper.CourseExerciseMapper;
import com.tfjy.sda.mapper.TaskListMapper;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.service.TaskListService;
import com.tfjy.sda.util.SpringUtil;
import com.tfjy.sda.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
/*
 * @ClassName: TaskListServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/6 10:17
 */



@Service
public class TaskListServiceImpl implements TaskListService {

    @Autowired
    private TaskListMapper taskListMapper;
    @Autowired
    private CourseExerciseMapper courseExerciseMapper;
    @Autowired
    private CourseFeignService courseFeignService;
    @Override
    public void getTaskList() {
        //获取登录方法
        ChromeDriver driver = courseFeignService.login();
        //获取所有的课程的作业链接
        List<CourseExercise> courseExercises = courseExerciseMapper.selectAll();
        for (CourseExercise courseExercise : courseExercises) {
            //进入课程详情页面
           driver.get( courseExercise.getTaskUrl());
           //获取课程ID
            String courseId = courseExercise.getCourseId();

            WebElement rightCon = driver.findElement(By.className("RightCon"));
            String task = rightCon.getAttribute("outerHTML");
            Document document = Jsoup.parse(task);

            //判断是否存在分页数据
            if (document.select(".page").select("span").text().equals("首页 < 1")){
                //需要分页，获取分页数据
                //第一页
                String page1 = document.select("span[class=current]").text();
                //获取第一页页面数据
                getTaksList(courseId,document);
                //后面页数
                String pageName = document.select(".page").select("a").text();
                System.out.println("原始字符串"+pageName);
                String ss= new StringBuffer(pageName).reverse().toString();
                System.out.println("反转字符串"+ss);
                //反转字符串，切割字符串
                String[] pageList = new StringBuffer(pageName).reverse().toString().substring(5).split("");
                //循环点击
                for (int i = 0; i < pageList.length; i++) {
                    System.out.println("pageList["+i+"]"+pageList[i]);
                    //定位第二页至后面页码位置
                    WebElement page = driver.findElement(By.className("page")).findElement(By.linkText(pageList[i]));
                    page.click();
                    //获取页面数据
                    WebElement rightCon1 = driver.findElement(By.className("RightCon"));
                    String task1 = rightCon1.getAttribute("outerHTML");
                    Document document1 = Jsoup.parse(task1);
                    getTaksList(courseId,document1);
                }
            }else {
                //不需要分页
                getTaksList(courseId,document);
            }

        }

        driver.close();
    }
    /*
     * @Description TODO 获取作业列表
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/6 20:20
     */
    private void getTaksList(String courseId, Document document) {
        Elements taskul = document.getElementsByTag("ul");
        for (Element ultask : taskul) {
            //获取li标签内容
            Elements taskli = ultask.getElementsByTag("li");
            for (Element litask : taskli) {
                if (litask.select(".titTxt").attr("class").equals("titTxt")){
                    String taskName = litask.select(".titTxt").select("p").select("a").text();
                    System.out.println("作业名称："+taskName);
                    String startTime = litask.select(".pt5").first().select("i").text();
                    System.out.println("开始时间："+startTime);
                    String stopTime = litask.select("#eTime").select("i").text();
                    System.out.println("结束时间："+stopTime);
                    String mutualTime = litask.select("#evaluation").select("i").text();
                    System.out.println("互评时间："+mutualTime);
                    String submitNum = litask.select("span[class=pt5]").select("i").text().substring(34).trim();
                    System.out.println("提交数： "+submitNum);
                    String pendingNum = litask.select(".titOper").select("strong").text();
                    System.out.println("评论数："+pendingNum);
                    String taskUrl = "https://mooc1-1.chaoxing.com"+litask.select(".Btn_blue_1").select(".fr").attr("href");
                    System.out.println("作业链接："+taskUrl);
                    //查询是否存在该作业
                    Example example = new Example(TaskList.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("taskName",taskName);
                    criteria.andEqualTo("startTime",startTime);
                    int taskNum = taskListMapper.selectCountByExample(example);
                    if (taskNum!=0){
                        System.out.println("该作业已存在，请勿重复添加");
                    }else {
                        //作业不存在添加记录
                        TaskList taskList = new TaskList();
                        taskList.setId(IdUtil.randomUUID());
                        taskList.setTaskName(taskName);
                        taskList.setStartTime(startTime);
                        taskList.setStopTime(stopTime);
                        taskList.setMutualTime(mutualTime);
                        taskList.setSubmitNum(submitNum);
                        taskList.setPendingNum(pendingNum);
                        taskList.setTaskUrl(taskUrl);
                        taskList.setCourseId(courseId);
                        taskListMapper.insert(taskList);
                        System.out.println("添加课程成功");
                    }
                }
            }
        }
    }


}
