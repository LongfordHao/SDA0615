package com.tfjy.sda.bean;

/*
 * @ClassName: TaskDetail
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/14 8:32
 */

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
@Data
public class TaskDetail implements Serializable {
    @Id
    @Column
    private String Id;
    @Column
    private String stuNum;
    @Column
    private String taskContent;
    @Column
    private String taskUrl;
    @Column
    private String taskStaId;
    @Column
    private String taskId;
}
