package com.tfjy.sda.Bean;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/*
 * @ClassName: Topic
 * @description //TODO topic实体类
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/5/31 15:28
 */
@Data
public class Topic implements Serializable {
    @Id
    @Column
    private int Id;//主键ID
    @Column
    private String teacherName;//老师姓名
    @Column
    private String topicContent;//讨论内容
    @Column
    private String topicTime;//发布讨论时间
}
