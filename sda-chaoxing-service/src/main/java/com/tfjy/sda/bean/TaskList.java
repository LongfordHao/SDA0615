package com.tfjy.sda.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * @ClassName: TaskList
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/6 10:08
 */
@Data
public class TaskList implements Serializable {
    @Id
    @Column
    private String Id;
    @Column
    private String taskName;
    @Column
    private String startTime;
    @Column
    private String stopTime;
    @Column
    private String mutualTime;
    @Column
    private String submitNum;
    @Column
    private String pendingNum;
    @Column
    private String taskUrl;
    @Column
    private  String courseId;
}
