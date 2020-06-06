package com.tfjy.sda.util;

/*
 * @ClassName: StringUtil
 * @description TODO
 * @Version:V1.0
 * @author: 张兴军
 * @date: 2020/6/6 17:22
 */

public class StringUtil {
    /*
     * @Description //TODO 反转字符串
     * @param:
     * @return:
     * @author: 张兴军
     * @date: 2020/6/6 17:23
     */

    public   static  String getLastString(String source,int lenth){
        return source.length()>=lenth?source.substring((source.length()-lenth)):source;
    }
}
