package com.tfjy.sda.service.impl;


/*
 * @ClassName: CourseExerciseServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/4 20:29
 */

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.Course;
import com.tfjy.sda.CourseExercise;
import com.tfjy.sda.mapper.CourseExerciseMapper;
import com.tfjy.sda.mapper.CourseMapper;
import com.tfjy.sda.service.CourseExerciseService;
import com.tfjy.sda.service.CourseFeignService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CourseExerciseServiceImpl implements CourseExerciseService {

    @Autowired
    private CourseExerciseMapper courseExerciseMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseFeignService courseFeignService;
    @Override
    public void getexercise() {
        //调用登录方法
        ChromeDriver driver = courseFeignService.login();
        //查询课程表下面的课程URL
        List<Course> courses = courseMapper.selectAll();
        for (Course cours : courses) {
            String courseUrl = cours.getCourseUrl();
            driver.get(courseUrl);
            System.out.println("课程名称："+cours.getCourseName());
            //获取课程作业与讨论链接;
            String taskURL = driver.findElement(By.partialLinkText("作业")).getAttribute("href");
            System.out.println("作业："+taskURL);
            String topicURL = driver.findElement(By.partialLinkText("讨论")).getAttribute("href");
            System.out.println("讨论："+topicURL);
            System.out.println("==========================================================");
            //保存到数据库
            //查询数据库是否又该课程活动内容
            Example example = new Example(CourseExercise.class);
            example.createCriteria().andEqualTo("courseId",cours.getId());
            int exercise = courseExerciseMapper.selectCountByExample(example);
            if (exercise!=0){
                System.out.println("该课程已有活动，课程名称："+cours.getCourseName());
            }else {
                CourseExercise courseExercise = new CourseExercise();
                courseExercise.setId(IdUtil.randomUUID());
                courseExercise.setTopicUrl(topicURL);
                courseExercise.setTaskUrl(taskURL);
                courseExercise.setCourseId(cours.getId());
                courseExerciseMapper.insert(courseExercise);
            }
        }
       driver.close();
    }
}
