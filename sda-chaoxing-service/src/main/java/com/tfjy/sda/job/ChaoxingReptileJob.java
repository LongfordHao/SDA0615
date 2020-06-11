package com.tfjy.sda.job;/*
 * @ClassName: ChaoxingReptileJob
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/9 17:44
 */

import com.tfjy.sda.service.CourseFeignService;
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
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");
        courseFeignService.homePage();
        XxlJobLogger.log(this.getClass().getSimpleName() + "--start");
        return ReturnT.SUCCESS;
    }
}
