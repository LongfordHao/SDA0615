package com.tfjy.sda.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Author: LangFordHao
 * Version:
 * Date: 2020/6/10
 * Time: 21:04
 * Description:
 */
@Data
public class UserInfo {
    @Id
    @Column
    private String Id;
    @Column
    private String studentNumber;
    @Column
    private String name;
    @Column
    private String role;
    @Column
    private String phone;
    @Column
    private String school;
    @Column
    private String department;
    @Column
    private String className;
    @Column
    private String courseId;

}
