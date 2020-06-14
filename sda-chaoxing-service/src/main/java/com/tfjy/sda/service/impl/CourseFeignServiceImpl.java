package com.tfjy.sda.service.impl;

import cn.hutool.core.util.IdUtil;
import com.tfjy.sda.bean.Course;
import com.tfjy.sda.mapper.CourseMapper;
import com.tfjy.sda.service.CourseFeignService;
import com.tfjy.sda.util.PropertiesUtil;
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
 * @ClassName: CourseFeignServiceImpl
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/4 17:10
 */


@Service
public class CourseFeignServiceImpl implements CourseFeignService {

    @Autowired
    private CourseMapper courseMapper;
    /*
     * @Description //TODO 学习通登录方法
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/2 15:39
     */
    @Override
    public ChromeDriver login() {
        System.setProperty("webdriver.chrome.driver", "D:/Google/chromedriver.exe");
        //初始化浏览器
        ChromeDriver chromeDriver = new ChromeDriver();
        //打开网页
        String login = PropertiesUtil.getValue("chaoxing.login");
        chromeDriver.get(login);

        try {
            //输入账号
            WebElement phone = chromeDriver.findElement(By.id("phone"));
            String usrname = PropertiesUtil.getValue("chaoxing.uname");
            phone.sendKeys(usrname);
            Thread.sleep(2000);
            //输入密码
            WebElement pwd = chromeDriver.findElement(By.id("pwd"));
            String password = PropertiesUtil.getValue("chaoxing.password");
            pwd.sendKeys(password);
            Thread.sleep(2000);
            //点击登录
            WebElement loginBtn = chromeDriver.findElement(By.id("loginBtn"));
            loginBtn.click();
            Thread.sleep(2000);
            return chromeDriver;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return chromeDriver;
    }

    /*
     * @Description //TODO  获取课程首页信息
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/4 14:49
     */
    @Override
    public void homePage() {
        ChromeDriver driver = login();
        String curriculumUrl = PropertiesUtil.getValue("curriculum.url");
        //进入首页
        driver.get(curriculumUrl);
        //获取首页，课程信息
        WebElement element = driver.findElement(By.xpath("/html"));
        String html = element.getAttribute("outerHTML");
        Document curriculum = Jsoup.parse(html);
        Elements ulDiv = curriculum.select(".ulDiv");
        //获取ul标签内容
        Elements ulelement = curriculum.getElementsByTag("ul");
        for (Element elementul : ulelement) {
            //获取li标签内容
            Elements lielement = elementul.getElementsByTag("li");
            for (Element elementli : lielement) {
                //判断是否为课程内容
                if (elementli.attr("style").equals("position:relative")){
                    //课程名称
                    String courseName = elementli.select("h3").select("a").attr("title");
                    System.out.println(courseName);
                    //课程ulr
                    String courseUrl = elementli.select("h3").select("a").attr("abs:href");
                    System.out.println(courseUrl);
                    //任课老师
                    String courseTeacher = elementli.select("p").first().text();
                    System.out.println(courseTeacher);
                    //课程备注
                    String courseExplain = elementli.select("p").select("p[^title]").text();
                    System.out.println(courseExplain);

                    //查询数据库是否存在该课程
                    Example example = new Example(Course.class);
                    example.createCriteria().andEqualTo("courseName", courseName);
                    int courses = courseMapper.selectCountByExample(example);
                    if (courses!=0){
                        System.out.println("该课程已经存在，课程名称："+ courseName);
                    }else {
                        //该课程不存在，添加课程
                        Course course = new Course();
                        course.setId(IdUtil.randomUUID());
                        course.setCourseName(courseName);
                        course.setCourseTeacher(courseTeacher);
                        course.setCourseExplain(courseExplain);
                        course.setCourseUrl(courseUrl);
                        courseMapper.insert(course);
                    }
                }

            }
        }
        driver.close();
    }
}
