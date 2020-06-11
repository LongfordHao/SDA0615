package com.tfjy.sda.bean;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * @ClassName: course
 * @description // TODO 课程表信息
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/4 16:50
 */
@Data
public class Course implements Serializable {
    @Id
    @Column
    private String Id;//主键ID
    @Column
    private String courseName;//课程名称
    @Column
    private String courseTeacher;//任课老师
    @Column
    private String courseExplain;//课程备注
    @Column
    private String courseUrl;//课程链接
}
