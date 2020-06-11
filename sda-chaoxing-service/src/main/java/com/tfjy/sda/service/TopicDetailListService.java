package com.tfjy.sda.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @description:讨论详情的接口
 * @Version:V1.0
 * @params
 * @return
 * @auther: 刘一鸣
 * @date: 2020-06-07 11:20
 */
@Component//把普通pojo实例化到spring容器中。当类不属于各种归类的时候需要用这个。component的英文意思是组成部分；成分；部件的意思。
@FeignClient(value = "CHAOXING-SERVICE")//value值为服务提供方服务名称。必须指定。feign：假装的意思。
public interface TopicDetailListService {
    //获取讨论详情内容的方法 --刘一鸣
    void getDetailList() throws InterruptedException;
}
