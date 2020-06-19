package com.tfjy.sda.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/10
 * Time: 10:07
 * Description:根据加分规则给学生加分
 */
@Component
@FeignClient(value = "CHAOXING-SERVICE")
public interface IntegralService {
    /**
     * 根据学生发布的讨论主题中的关键子给学生加分，加分对象为问题的发起人。基础分为一分，只要学生发布了讨论主题就有一分。
     */
        void getIntegralInto();

    /**
     * 根据话题id查询话题下所有加分学生的信息
     * @param topicId
     * @return
     */
    List questIntegralList(String topicId);

    /**
     * 根据话题id查询话题下所有学生的信息，分页
     * @param topicId
     * @param page
     * @return
     */
    Object questIntegralListPage(String topicId, int page);

    /**
     * 课程下所有加分学生的信息
     * @param courseId
     * @param page
     * @param size
     * @return
     */
    Object questCourseIntegralListPage(String courseId,int page ,int size);

    /**
     * 学生的加分成绩折线图
     * @param studentNO
     * @return
     */
    List questStudentIntegrall (String studentNO);
}
