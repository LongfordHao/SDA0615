package com.tfjy.sda.service;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;
/*
 * @ClassName: CourseFeignService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:04
 */


@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface CourseFeignService {
    //学习通登录方法
    ChromeDriver login();
    //获取课程首页信息
    String homePage();
    //获取课程信息
    List queryCourse();
}
