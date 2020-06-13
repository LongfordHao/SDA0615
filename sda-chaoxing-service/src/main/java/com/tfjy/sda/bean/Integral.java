package com.tfjy.sda.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Author: LangFordHao
 * Version:V1.0
 * Date: 2020/6/10
 * Time: 10:14
 * Description:给学生加分的表
 */
@Data
public class Integral implements Serializable {
    @Id
    @Column
    private String Id;
    @Column
    private String stuNumber;
    @Column
    private String name;
    @Column
    private String integral;
    @Column
    private String topicId;
    @Column
    private String remarks;
}
