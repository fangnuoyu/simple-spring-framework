package com.hrt.example;

import com.hrt.example.config.OwnConfig;
import com.hrt.example.core.SimpleApplicationContext;
import com.hrt.example.service.OrderServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleSpringFrameworkApplication {

    public static void main(String[] args) {

        SpringApplication.run(SimpleSpringFrameworkApplication.class, args);
        SimpleApplicationContext simpleApplicationContext = new SimpleApplicationContext(OwnConfig.class);
//        UserService userService = (UserService)simpleApplicationContext.getBean("userServiceImpl");
//        userService.test();
        OrderServiceImpl orderServiceImpl = (OrderServiceImpl)simpleApplicationContext.getBean("orderServiceImpl");
        orderServiceImpl.test();

    }

}
