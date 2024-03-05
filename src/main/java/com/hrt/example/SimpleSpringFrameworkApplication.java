package com.hrt.example;

import com.hrt.example.config.OwnConfig;
import com.hrt.example.core.SimpleApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleSpringFrameworkApplication {

    public static void main(String[] args) {

        SpringApplication.run(SimpleSpringFrameworkApplication.class, args);
        SimpleApplicationContext simpleApplicationContext = new SimpleApplicationContext(OwnConfig.class);
        System.out.println(simpleApplicationContext.getBean("userServiceImpl"));
        System.out.println(simpleApplicationContext.getBean("userServiceImpl"));
        System.out.println(simpleApplicationContext.getBean("userServiceImpl"));
        System.out.println(simpleApplicationContext.getBean("userServiceImpl"));
    }

}
