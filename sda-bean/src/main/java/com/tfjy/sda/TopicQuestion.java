package com.tfjy.sda;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @description
 * @Version:V1.0
 * @params
 * @return
 * @auther: 刘一鸣
 * @date: 2020-06-07 10:42
 */
@Data
public class TopicQuestion implements Serializable {
    @Id //表示此属性需要映射到数据库中主键的字段。
    @Column //实体类中的属性可以和数据库表中的列名对应
    private String id;
    @Column
    private String questioner;
    @Column
    private String questionContent;
    @Column
    private String questionTime;
    @Column
    private String thumbsUp;
    @Column
    private String topicId;
    @Column
    private String score;
}
