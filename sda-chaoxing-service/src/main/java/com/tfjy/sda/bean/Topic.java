package com.tfjy.sda.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * @ClassName: Topic
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 14:36
 */
@Data
public class Topic implements Serializable {
    @Id
    @Column
    private String Id;
    @Column
    private String teacherName;
    @Column
    private String topicContent;
    @Column
    private String topicTime;
}
