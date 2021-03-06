package com.tfjy.sda.util;

/*
 * @description 非controller层调用server服务工具类
 * @Version:V1.0
 * @params
 * @return
 * @auther: 张兴军
 * @date: 2020/4/28 11:36
 */


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;

        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }
}


