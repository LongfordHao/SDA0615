package com.tfjy.sda.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/*
 * @ClassName: TaskStatisticsService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/7 8:27
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface TaskStatisticsService {
    //获取作业
    void getTask();
}
