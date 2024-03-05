package com.hrt.example.service;

import com.hrt.example.annotation.Component;
import com.hrt.example.annotation.Scope;

/**
 * @Name UserServiceImpl
 * @Description UserServiceImpl
 * @Author HRT
 * @Date 2024/3/5 10:33
 * @Version 1.0.0
 **/
@Component("userServiceImpl")
@Scope("singleton")
public class UserServiceImpl {

    public void test() {
        System.out.println("UserServiceImpl");
    }
}
