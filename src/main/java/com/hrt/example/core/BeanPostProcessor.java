package com.hrt.example.core;

/**
 * @Name BeanPostProcessor
 * @Description BeanPostProcessor
 * @Author HRT
 * @Date 2024/3/5 12:52
 * @Version 1.0.0
 **/
public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(String beanName, Object bean);

    Object postProcessAfterInitialization(String beanName, Object bean);
}
