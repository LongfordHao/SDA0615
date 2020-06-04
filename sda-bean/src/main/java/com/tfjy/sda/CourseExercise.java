package com.tfjy.sda;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
/*
 * @ClassName: CourseExercise
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/4 20:23
 */


@Data
public class CourseExercise implements Serializable {
    @Id
    @Column
    private String Id;
    @Column
    private String topicUrl;
    @Column
    private String taskUrl;
    @Column
    private String courseId;
}
