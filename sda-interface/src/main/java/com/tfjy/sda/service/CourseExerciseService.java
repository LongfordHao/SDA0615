package com.tfjy.sda.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface CourseExerciseService {
    //获取课程活动链接
    void getexercise();
}
