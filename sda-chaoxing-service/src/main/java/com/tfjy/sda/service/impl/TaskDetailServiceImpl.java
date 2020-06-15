package com.tfjy.sda.service.impl;/*
 * @ClassName: TaskDetailServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/14 8:34
 */

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.tfjy.sda.bean.TaskDetail;
import com.tfjy.sda.bean.TaskList;
import com.tfjy.sda.bean.TaskStatistics;
import com.tfjy.sda.mapper.TaskDetailMapper;
import com.tfjy.sda.mapper.TaskListMapper;
import com.tfjy.sda.mapper.TaskStatisticsMapper;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.service.TaskDetailService;
import com.tfjy.sda.service.TaskListService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Service
public class TaskDetailServiceImpl implements TaskDetailService {
    @Autowired
    private CourseFeignService courseFeignService;
    @Autowired
    private TaskListMapper taskListMapper;
    @Autowired
    private TaskStatisticsMapper taskStatisticsMapper;
    @Autowired
    private TaskDetailMapper taskDetailMapper;

    @Override
    public void getTaskDetailList() {
        //获取登录信息
        ChromeDriver driver = courseFeignService.login();
        //查询作业列表内容
        List<TaskList> taskList = taskListMapper.selectAll();
        for (TaskList list : taskList) {
            //判断是否为老师评分
            if (list.getTaskType()==null){
                //获取作业Id
                String taskId = list.getId();
                //根据作业id查询作业统计中该作业统计信息
                Example taskStatistics = new Example(TaskStatistics.class);
                taskStatistics.createCriteria().andEqualTo("taskId",taskId);
                List<TaskStatistics> taskStat = taskStatisticsMapper.selectByExample(taskStatistics);
                for (TaskStatistics statistics : taskStat) {
                    //判断批阅状态是否为待审批
                    if (statistics.getReviewStatus().equals("待批阅")){
                        //获取作业统计Id
                        String statisticsId = statistics.getId();
                        //获取作业详情链接
                        String taskUrl = statistics.getTaskUrl();
                        //进入学生个人作业页面
                        driver.get(taskUrl);
                        //获取页面数据元素
                        WebElement tableTask = driver.findElement(By.className("CeYan"));
                        String taskHtml = tableTask.getAttribute("outerHTML");
                        Document document = Jsoup.parse(taskHtml);
                        //获取作业数据
                        Elements timu = document.select(".TiMu.imgReview");
                        //创建作业详情集合
                        ArrayList<String> arrayList = new ArrayList<>();
                        HashMap<String, String> map = new HashMap<>();
                        //获取学生姓名
                        String stuName = driver.findElement(By.id("stuRealName")).getAttribute("value");
                        //获取学号
                        String stuNum = document.select("span:contains(学号/账号：)").select("i").text();
                        //String stuNum = driver.findElement(By.className("ZyTop")).findElement(By.linkText("学号/账号：")) .findElement(By.tagName("i")).getText();
                        System.out.println("姓名"+stuName);
                        System.out.println("学号"+stuNum);
                        for (int i = 0; i < timu.size(); i++) {
                            //获取其中一个作业
                            Element elementTask = timu.get(i);
                            //获取作业描述
                            String taskContent = elementTask.select(".clearfix.questionTitle").text().trim();
                            //获取作业答案
                            String answer = elementTask.select(".stuAnswer.stuAnswerArea").text().trim();
                            //获取最高分值
                            String str = elementTask.select(".makescore.font14").select("span").text();
                            String highestScore = str.substring(str.indexOf("："),str.indexOf(" 分"));
                            map.put("taskContent",taskContent);
                            map.put("answer",answer);
                            map.put("highestScore",highestScore);
                            arrayList.add(JSON.toJSONString(map));
                            System.out.println(arrayList);
                            System.out.println("作业描述："+taskContent);
                            System.out.println("作业答案："+answer);
                            System.out.println("最高分值："+highestScore);
                        }
                        //实体赋值
                        TaskDetail taskDetail = new TaskDetail();
                        taskDetail.setId(IdUtil.randomUUID());
                        taskDetail.setStuNum(stuNum);
                        taskDetail.setTaskUrl(taskUrl);
                        taskDetail.setTaskStaId(statisticsId);
                        taskDetail.setTaskContent(arrayList.toString());
                        taskDetail.setTaskId(taskId);
                        //判断是否存在该信息记录，通过作业id查询
                        Example example = new Example(TaskDetail.class);
//                        example.createCriteria().andEqualTo("stuNum",stuNum);
                        example.createCriteria().andEqualTo("taskStaId",statisticsId);
                        List<TaskDetail> taskDetails = taskDetailMapper.selectByExample(example);
                        TaskDetail taskDetail1 = taskDetailMapper.selectOneByExample(example);

                        if (taskDetails!=null && taskDetails.size()>0){
                            //遍历循环取出id
                            String id=null;
                            for (TaskDetail detail : taskDetails) {
                               id= detail.getId();
                            }
                            taskDetail.setId(id);
                            //更新该记录
                            taskDetailMapper.updateByPrimaryKey(taskDetail);
                            System.out.println("已添加作业，请勿重复");
                        }else {
                            //插入数据
                            taskDetailMapper.insert(taskDetail);
                            System.out.println("添加作业成功！！！");
                        }
                    }
                }
            }

        }
    }
}
