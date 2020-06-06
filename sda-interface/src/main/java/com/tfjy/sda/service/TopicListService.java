package com.tfjy.sda.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/6/5
 * Time: 9:44
 * Description:
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface TopicListService {
    //获取讨论列表
    void getTopicList();
}
