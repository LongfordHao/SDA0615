package com.tfjy.sda;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/*
 * @ClassName: TaskStatistics
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/7 8:20
 */
@Data
public class TaskStatistics {
    @Id
    @Column
    private  String Id;//主键ID
    @Column
    private  String stuName;//学生姓名
    @Column
    private String stuNumber;//学号
    @Column
    private String reviewStatus;//审批状态
    @Column
    private String submitTime;//提交时间
    @Column
    private String stuIp;//提交IP
    @Column
    private String reviewTime;//批阅时间
    @Column
    private String reviewer;//审批人
    @Column
    private String reviewerIp;//审批人IP
    @Column
    private String result;//成绩
    @Column
    private String taskUrl;//作业详情链接
    @Column
    private  String taskId;//作业Id
}
