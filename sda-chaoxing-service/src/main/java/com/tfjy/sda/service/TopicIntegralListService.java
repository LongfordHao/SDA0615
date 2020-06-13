package com.tfjy.sda.service;

import com.tfjy.sda.bean.TopicUrlModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/13
 * Time: 9:37
 * Description:
 */
@Component
public interface TopicIntegralListService {
    List queryIntegralList(TopicUrlModel topicUrl);
}
