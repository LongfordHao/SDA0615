package com.tfjy.sda.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @description  判断元素是否存在
 * @Version:V1.0
 * @params
 * @return
 * @auther: 张兴军
 * @date: 2020/5/13 11:08
 */
public class ElementUtil {
    public static Boolean check(WebDriver driver, By seletor) {
        try {
            driver.findElement(seletor);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    /**
     *
     * 功能描述: 反转字符串
     *
     * @param:
     * @return:
     * @auther: 张兴军
     * @date: 2020/5/13 15:28
     */

    public   static  String getLastString(String source,int lenth){
        return source.length()>=lenth?source.substring((source.length()-lenth)):source;
    }

}
