package com.tfjy.sda.mapper;

import com.tfjy.sda.TopicList;
import com.tfjy.sda.TopicReply;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description
 * @Version:V1.0
 * @params
 * @return
 * @auther: 刘一鸣
 * @date: 2020-06-07 15:07
 */
@Service
public interface TopicDetailListMapper extends Mapper<TopicReply> {

}
