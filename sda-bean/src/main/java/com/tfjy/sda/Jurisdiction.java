package com.tfjy.sda;/*
 * @ClassName: Jurisdiction
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/10 9:44
 */

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class Jurisdiction  implements Serializable {
    @Column
    private String password;
    @Column
    private  String userCode;
}
