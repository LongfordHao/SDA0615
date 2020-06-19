package com.tfjy.sda.controller;

import com.tfjy.sda.bean.Topic;
import com.tfjy.sda.bean.TopicUrlModel;
import com.tfjy.sda.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*
 * @ClassName: ChaoxingProviderControlle
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:01
 */
@CrossOrigin
//@Api(tags = {"学习通模块接口"})
@RequestMapping(value = "/chaoxing")
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
    @Resource
    private TopicDetailListService topicDetailListService;
    @Resource
    private IntegralService integralService;
    @Resource
    private TopicIntegralListService topicIntegralListService;

    @GetMapping("/all")
    public List<Topic> queryAll(){
       return topicFeignService.queryAll();
    }
    @GetMapping("/test")
    public String queryTest(){
        return this.topicFeignService.queryTest();
    }
//    @ApiOperation(value = "登录")
    @GetMapping("/login")
    public void login(){
        courseFeignService.login();
    }
//    @ApiOperation(value = "获取首页课程")
    @GetMapping("/home")
    public void homePage(){
        courseFeignService.homePage();
    }
//    @ApiOperation(value = "获取课程活动")
    @GetMapping("/exercise")
    public void  getexercise(){
        courseExerciseService.getexercise();
    }
//    @ApiOperation(value = "获取讨论列表")
    @GetMapping("/topicList")
    public void  getTopicList(){
        try {
            topicListService.getTopicList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//    @ApiOperation(value = "获取作业列表")
    @GetMapping("/taskList")
    public void getTaskList(){
        taskListService.getTaskList();
    }
//    @ApiOperation(value = "获取作业统计")
    @GetMapping("task")
    public void getTask(){
        taskStatisticsService.getTask();
    }
    //@ApiOperation(value="获取讨论话题的问题列表和讨论详情列表")
    @RequestMapping("topicDetailList")
    private void  getDetailList()  {
        try {
            topicDetailListService.getDetailList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("integralQuestionAdd")
    public void  integralQuestrionAdd(){
        integralService.getIntegralInto();
    }

    @PostMapping("queryIntegralList" )
    public  List queryIntegralList(@RequestBody TopicUrlModel topicUrl){
        return topicIntegralListService.queryIntegralList(topicUrl);
    }
    @GetMapping("/queryCourse")
    public List queryCourse(){
      return  courseFeignService.queryCourse();
    }

    @ApiOperation(value="获取话题下学生加分列表内容")
    @GetMapping("/questIntegralList")
    public List  questIntegralList(@RequestParam("topicId") String topicId){
        return integralService.questIntegralList(topicId);
    }

    @ApiOperation(value="获取话题下学生加分列表内容分页")
    @GetMapping("/questIntegralListPage")
    public Object  questIntegralListPage(@RequestParam("topicId") String topicId,@RequestParam("page") int page){
        return integralService.questIntegralListPage(topicId,page);
    }

    @ApiOperation(value="获取讨论列表内容")
    @GetMapping("/questTopicList")
    public List  questTopicList(@RequestParam("courseId") String courseId){
        return topicListService.questTopicList(courseId);
    }

    @ApiOperation(value = "课程下所有加分学生的信息")
    @GetMapping("/questCourseIntegralListPage")
    public Object questCourseIntegralListPage(@RequestParam("courseId") String courseId,@RequestParam("page") int page,@RequestParam("size") int size){
        return integralService.questCourseIntegralListPage(courseId,page,size);
    }

    @ApiOperation(value = "学生的加分成绩折线图")
    @GetMapping("/questStudentIntegral")
    public List questStudentIntegral (String studentNo){
        return integralService.questStudentIntegrall(studentNo);
    }
}
