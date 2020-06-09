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
 * @date: 2020-06-07 11:02
 */
@Data
public class TopicReply implements Serializable {
    @Id
    @Column
    private String id;
    @Column
    private String replyName;
    @Column
    private String replied;
    @Column
    private String replyContent;
    @Column
    private String replyTim;
    @Column
    private String questionId;
}
