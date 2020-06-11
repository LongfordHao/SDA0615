package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.bean.TaskList;
import com.tfjy.sda.bean.TaskStatistics;

import com.tfjy.sda.mapper.TaskListMapper;
import com.tfjy.sda.mapper.TaskStatisticsMapper;
import com.tfjy.sda.service.CourseFeignService;

import com.tfjy.sda.service.TaskStatisticsService;
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
 * @ClassName: TaskStatisticsServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/7 8:27
 */



@Service
public class TaskStatisticsServiceImpl implements TaskStatisticsService {

    @Autowired
    private TaskStatisticsMapper taskStatisticsMapper;
    @Autowired
    private TaskListMapper taskListMapper;
    @Autowired
    private CourseFeignService courseFeignService;
    @Override
    public void getTask() {
        //获取登录方法
        ChromeDriver driver = courseFeignService.login();
        //获取作业列表中的具体作业内容
        List<TaskList> taskList = taskListMapper.selectAll();
        for (TaskList task : taskList) {
            //作业列表id
            String taskId = task.getId();
            //获得提交作业数量
            String submit = task.getSubmitNum();
            String subnum = submit.substring(0, submit.indexOf("/"));
            int num=0;
            if (Integer.parseInt(subnum)%15!=0){
                num=Integer.parseInt(subnum)/15+1;
            }else {
                num=Integer.parseInt(subnum)/15;
            }
            driver.get( task.getTaskUrl());
            //获取数据
            String attribute = driver.findElement(By.id("markList")).getAttribute("outerHTML");
            Document parse = Jsoup.parse(attribute);

            //判断是否存在分页数据
            if (parse.select(".page").select("span").text().contains("首页")) {
                //获取第一页内容
                getTaskcent(driver, taskId);
                //后面页数
                for (int i = 1; i < num; i++) {
                    //点击下一页，获取下一页内容
                    WebElement webElement =driver.findElement(By.className("page")).findElement(By.linkText(">"));
                    webElement.click();
                    //获取页面数据
                    getTaskcent(driver,taskId);
                }
            }else {
                //不需要分页
                getTaskcent(driver,taskId);
            }
        }
        //关闭浏览器
        driver.close();
    }
    /*
     * @Description TODO 获取作业内容
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/7 10:36
     */
    private void getTaskcent(ChromeDriver driver, String taskId) {
        //暂停半秒，加载数据
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebElement tableTask = driver.findElement(By.id("markList"));
        String taskHtml = tableTask.getAttribute("outerHTML");
        Document document = Jsoup.parse(taskHtml);
        //获取tr标签内容
        Elements tasktr = document.select("tr");

        for (int i = 1; i < tasktr.size(); i++) {
            //获取一个tr
            Element taskelement = tasktr.get(i);
            //获取该tr下面的td
            Elements tasktd = taskelement.select("td");
            TaskStatistics taskStatistics = new TaskStatistics();
            //姓名
            String stuNume=null;
            //学号
            String stuNumber=null;
            for (int j = 0; j < tasktd.size(); j++) {
                String text = tasktd.get(j).text();
                System.out.println("索引号："+j+"..."+text);
                switch (j){
                    case 0:
                        //姓名
                        stuNume=tasktd.get(j).text();
                        taskStatistics.setStuName(tasktd.get(j).text());
                        break;
                    case 1:
                        //学号
                        stuNumber=tasktd.get(j).text();
                        taskStatistics.setStuNumber(tasktd.get(j).text());
                        break;
                    case 2:
                        //状态
                        taskStatistics.setReviewStatus(tasktd.get(j).text());
                        break;
                    case 3:
                        //提交日期
                        taskStatistics.setSubmitTime(tasktd.get(j).text());
                        break;
                    case 4:
                        //提交IP
                        taskStatistics.setStuIp(tasktd.get(j).text());
                        break;
                    case 5:
                        //批阅时间
                        taskStatistics.setReviewTime(tasktd.get(j).text());
                        break;
                    case 6:
                        //批阅人
                        taskStatistics.setReviewer(tasktd.get(j).text());
                        break;
                    case 7:
                        //批阅ip
                        taskStatistics.setReviewerIp(tasktd.get(j).text());
                        break;
                    case 8:
                        //成绩
                        taskStatistics.setResult(tasktd.get(j).text());
                        break;
                    case 9:
                        break;
                    default:
                        break;
                }
            }
            System.out.println("==============这是个人作业==============");
            String onclick = taskelement.select("a[href=javascript:void(0)]").attr("onclick").substring(15);
            //切割字符串反转
            String substring = new StringBuffer(onclick).reverse().toString().substring(2);
            String taskUrl="https://mooc1-1.chaoxing.com"+new StringBuffer(substring).reverse().toString();
            System.out.println(taskUrl);
            //作业详情url
            taskStatistics.setTaskUrl(taskUrl);
            //主键id
            taskStatistics.setId(IdUtil.randomUUID());
            //作业列表ID
            taskStatistics.setTaskId(taskId);
            //判断数据是否存在数据
            Example example = new Example(TaskStatistics.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("stuName",stuNume);
            criteria.andEqualTo("stuNumber",stuNumber);
            criteria.andEqualTo("taskId",taskId);
//                    int count = taskStatisticsMapper.selectCountByExample(example);
            List<TaskStatistics> taskStatistics1 = taskStatisticsMapper.selectByExample(example);

            if (taskStatistics1.size()>0 && taskStatistics1!=null){
                String id=null;
                for (TaskStatistics statistics : taskStatistics1) {
                    id = statistics.getId();
                }
                //更新数据
                taskStatistics.setId(id);
                taskStatisticsMapper.updateByPrimaryKey(taskStatistics);
            }else {

                //数据库不存在，插入该数据
                taskStatisticsMapper.insert(taskStatistics);
            }
        }
    }



}
