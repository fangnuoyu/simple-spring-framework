package com.hrt.example.service;

import com.hrt.example.annotation.Component;
import com.hrt.example.annotation.Scope;
import com.hrt.example.core.BeanNameAware;
import com.hrt.example.core.InitializingBean;

/**
 * @Name UserServiceImpl
 * @Description UserServiceImpl
 * @Author HRT
 * @Date 2024/3/5 10:33
 * @Version 1.0.0
 **/
@Component("userServiceImpl")
@Scope("singleton")
public class UserServiceImpl implements UserService, BeanNameAware, InitializingBean {

    public void test() {
        System.out.println("UserServiceImpl执行测试方法()");
    }

    @Override
    public void setBeanName(String beanName) {
        System.out.println("UserServiceImpl.Aware回调: " + beanName);
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化Bean");
    }

}
