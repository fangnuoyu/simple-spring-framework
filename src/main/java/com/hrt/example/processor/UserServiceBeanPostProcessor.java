package com.hrt.example.processor;

import com.hrt.example.annotation.Component;
import com.hrt.example.core.BeanPostProcessor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Name UserServiceBeanPostProcessor
 * @Description UserServiceBeanPostProcessor
 * @Author HRT
 * @Date 2024/3/5 12:57
 * @Version 1.0.0
 **/
@Component
public class UserServiceBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if(StringUtils.equals(beanName, "userServiceImpl")) {
            System.out.println("userServiceImpl初始化前UserServiceBeanPostProcessor: " + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if(StringUtils.equals(beanName, "userServiceImpl")) {
            System.out.println("userServiceImpl初始化后UserServiceBeanPostProcessor: " + beanName);
            Object proxyBean = Proxy.newProxyInstance(UserServiceBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("切面逻辑");
                    return method.invoke(bean, args);
                }
            });
            return proxyBean;
        }
        return bean;
    }
}
