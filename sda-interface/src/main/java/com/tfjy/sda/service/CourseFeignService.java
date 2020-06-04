package com.tfjy.sda.service;

import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;


@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface CourseFeignService {
    //学习通登录方法
    ChromeDriver login();
    //获取课程首页信息
    void homePage();
}
