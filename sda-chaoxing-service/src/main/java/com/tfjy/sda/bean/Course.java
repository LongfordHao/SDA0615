package com.tfjy.sda.bean;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Course：课程表")
public class Course implements Serializable {
    /**
     * 主键ID
     */
    @Id
    @Column
    @ApiModelProperty(value = "主键ID")
    private String Id;
    /**
     * 课程名称
     */
    @Column
    @ApiModelProperty(value = "课程名称")
    private String courseName;
    /**
     * 任课老师
     */
    @Column
    @ApiModelProperty(value = "任课老师")
    private String courseTeacher;
    /**
     * 课程备注
     */
    @Column
    @ApiModelProperty(value = "课程备注")
    private String courseExplain;
    /**
     * 课程链接
     */
    @Column
    @ApiModelProperty(value = "课程链接")
    private String courseUrl;
}
