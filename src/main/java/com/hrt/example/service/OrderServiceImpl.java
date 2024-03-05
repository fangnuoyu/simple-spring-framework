package com.hrt.example.service;

import com.hrt.example.annotation.Autowired;
import com.hrt.example.annotation.Component;

/**
 * @Name OrderServiceImpl
 * @Description OrderServiceImpl
 * @Author HRT
 * @Date 2024/3/5 11:40
 * @Version 1.0.0
 **/
@Component
public class OrderServiceImpl {
    @Autowired
    private UserService userServiceImpl;

    public void test() {
        System.out.println("OrderServiceImpl: " + userServiceImpl);
    }
}
