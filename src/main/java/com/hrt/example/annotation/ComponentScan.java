package com.hrt.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 注解在运行时有效
@Target({ElementType.TYPE, ElementType.METHOD}) // 注解可以应用于类和方法
public @interface ComponentScan {

    String value() default ""; // 默认没有值
}
