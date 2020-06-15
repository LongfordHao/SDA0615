package com.tfjy.sda.job;/*
 * @ClassName: ChaoxingReptileJob
 * @description //TODO 学习通定时任务爬取
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/9 17:44
 */

import com.tfjy.sda.service.*;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@JobHandler(value = "ChaoxingReptileJob")
@Component
public class ChaoxingReptileJob extends IJobHandler {
    @Autowired
    private CourseFeignService courseFeignService;
    @Autowired
    private CourseExerciseService courseExerciseService;
    @Autowired
    private TopicDetailListService topicDetailListService;
    @Autowired
    private TaskListService taskListService;
    @Autowired
    private TaskStatisticsService taskStatisticsService;
    @Autowired
    private TopicListService topicListService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");
        //课程首页
        courseFeignService.homePage();
        //课程活动
        courseExerciseService.getexercise();
        //讨论详情
        topicDetailListService.getDetailList();
        //作业列表
        taskListService.getTaskList();
        //作业统计
        taskStatisticsService.getTask();
        //讨论列表
        topicListService.getTopicList();
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");
        return ReturnT.SUCCESS;
    }
}
