package com.tfjy.sda.service.impl;

import com.tfjy.sda.bean.Integral;
import com.tfjy.sda.bean.TopicList;
import com.tfjy.sda.bean.TopicQuestion;
import com.tfjy.sda.bean.TopicUrlModel;
import com.tfjy.sda.mapper.IntegralMapper;
import com.tfjy.sda.mapper.TopicListMapper;
import com.tfjy.sda.mapper.TopicQuestionMapper;
import com.tfjy.sda.service.TopicIntegralListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/13
 * Time: 9:35
 * Description:教师查询某个话题下的所有学生加分情况
 */

@Service
public class TopicIntegralListImpl implements TopicIntegralListService {
    @Autowired
    private TopicListMapper topicListMapper;
    @Autowired
    private TopicQuestionMapper topicQuestionMapper;
    @Autowired
    private IntegralMapper integralMapper;

    /**
     * 通过讨论话题的网络地址来查询相关话题下的所有的问题的用户加分情况
     * @param topicUrl
     * @return
     */
    @Override
    public List queryIntegralList(TopicUrlModel topicUrl){
        //话题链接对应的topic_list 中的id
        Example exampleTopicList=new Example(TopicList.class);
        exampleTopicList.createCriteria().andEqualTo("topicUrl",topicUrl.getTopicUrl());
        TopicList topicList=topicListMapper.selectOneByExample(exampleTopicList);
        //在topic_question表中通过topic_id=id字段查询在同一话题下的question

        List integral=new ArrayList();
        if (topicList!=null){
            Example exampleQuestionList=new Example(TopicQuestion.class);
            exampleQuestionList.createCriteria().andEqualTo("topicId",topicList.getId());
            List<TopicQuestion> topicQuestions= topicQuestionMapper.selectByExample(exampleQuestionList);
            //遍历question 通过question 中的id=表integral中的topic_id 来查询 integral中的人员并保存到list中返回给前端。

            for ( TopicQuestion topicQuestion:topicQuestions) {
                Example exampleIntegral=new Example(Integral.class);
                exampleIntegral.createCriteria().andEqualTo("topicId",topicQuestion.getId());
                List integrals=integralMapper.selectByExample(exampleIntegral);
                if (integrals.size()!=0){
                    integral.add(integrals);
                }

            }
        }



        return  integral;
    }


}
