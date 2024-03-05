package com.hrt.example;

import com.hrt.example.config.OwnConfig;
import com.hrt.example.core.SimpleApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleSpringFrameworkApplicationTests {

    @Test
    void contextLoads() {

        SimpleApplicationContext simpleApplicationContext = new SimpleApplicationContext(OwnConfig.class);
    }

}
