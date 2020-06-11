package com.tfjy.sda.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/*
 * @ClassName: TaskListService
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 21:04
 */

@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface TaskListService {
    //获取作业列表
    void getTaskList();
}
