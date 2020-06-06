package com.tfjy.sda.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/*
 * @ClassName: CourseExerciseService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:04
 */

@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface CourseExerciseService {
    //获取课程活动链接
    void getexercise();
}
