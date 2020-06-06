package com.tfjy.sda;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/6/5
 * Time: 9:51
 * Description:
 */
@Data
public class TopicList implements Serializable {

    @Id
    @Column
    private String Id;
    @Column
    private String topicName;
    @Column
    private String topicConetnt;
    @Column
    private String topicTime;
    @Column
    private String topicUrl;
    @Column
    private String topicId;
}
