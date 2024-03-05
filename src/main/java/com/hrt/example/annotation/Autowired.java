package com.hrt.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 注解在运行时有效
@Target({ElementType.CONSTRUCTOR,ElementType.FIELD}) // 注解可以应用于构造方法和字段上
public @interface Autowired {
}
