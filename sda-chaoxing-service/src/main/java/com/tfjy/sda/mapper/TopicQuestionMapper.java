package com.tfjy.sda.mapper;

import com.tfjy.sda.TopicQuestion;
import com.tfjy.sda.TopicReply;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * @description
 * @Version:V1.0
 * @params
 * @return
 * @author: 刘一鸣
 * @date: 2020-06-08 10:50
 */
@Service
public interface TopicQuestionMapper extends Mapper<TopicQuestion> {
}
