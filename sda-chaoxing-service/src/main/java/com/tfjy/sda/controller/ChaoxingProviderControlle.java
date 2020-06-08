package com.tfjy.sda.controller;
import com.tfjy.sda.Topic;
import com.tfjy.sda.service.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
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
    @Resource
    private TopicListService topicListService;
    @Resource
    private CourseFeignService courseFeignService;
    @Resource
    private CourseExerciseService courseExerciseService;
    @Resource
    private TaskListService taskListService;
    @Resource
    private TaskStatisticsService taskStatisticsService;
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
        courseFeignService.login();
    }
    @RequestMapping("/home")
    public void homePage(){
        courseFeignService.homePage();
    }
    @RequestMapping("/exercise")
    public void  getexercise(){
        courseExerciseService.getexercise();
    }
    @RequestMapping("/topicList")
    public void  getTopicList(){
        try {
            topicListService.getTopicList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/taskList")
    public void getTaskList(){
        taskListService.getTaskList();
    }
    @RequestMapping("task")
    public void getTask(){
        taskStatisticsService.getTask();
    }
}
