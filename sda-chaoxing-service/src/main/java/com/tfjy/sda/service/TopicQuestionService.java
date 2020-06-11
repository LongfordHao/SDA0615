package com.tfjy.sda.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @description
 * @Version:V1.0
 * @params
 * @return
 * @auther: 刘一鸣
 * @date: 2020-06-08 10:54
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface TopicQuestionService {
    void getDetailList() throws InterruptedException;
}
