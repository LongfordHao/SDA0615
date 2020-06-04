package com.tfjy.sda;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/*
 * @ClassName: Topic
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/1 14:36
 */
@Data
public class Topic {
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
