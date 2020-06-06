package com.tfjy.sda.controller;
import com.tfjy.sda.Topic;
import com.tfjy.sda.service.CourseExerciseService;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.service.TopicFeignService;
import com.tfjy.sda.service.TopicListService;
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
}
