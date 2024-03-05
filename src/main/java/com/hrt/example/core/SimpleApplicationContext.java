package com.hrt.example.core;

import com.hrt.example.annotation.Component;
import com.hrt.example.annotation.ComponentScan;
import com.hrt.example.annotation.Scope;
import com.hrt.example.utils.FileUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Name SimpleApplicationContext
 * @Description 简易上下文(核心类)
 * @Author HRT
 * @Date 2024/3/4 10:14
 * @Version 1.0.0
 **/
public class SimpleApplicationContext {

    private Class configClass;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();

    public SimpleApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描--->BeanDefinition--->beanDefinitionMap
        if(configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String packageName = componentScanAnnotation.value();
            System.out.println("包名: " + packageName);

            String relativePath  = packageName.replace(".", "/");
            ClassLoader classLoader = this.getClass().getClassLoader();
            URL url = classLoader.getResource(relativePath);
            System.out.println("路径URL: " + url.getFile());
            File file = new File(url.getFile());
            List<String> fileAbsolutePathList = FileUtil.traverseDirectory(file);
            for (String fileAbsolutePath : fileAbsolutePathList) {
//                System.out.println("绝对路径: " + fileAbsolutePath);
                if(!fileAbsolutePath.endsWith(".class")) {
                    continue;
                }
                String fullClassName = fileAbsolutePath
                        .substring(fileAbsolutePath.indexOf("classes\\") + "classes\\".length())
                        .replace("\\", ".")
                        .replace(".class", "");
                Class<?> clazz = null;
                try {
                    System.out.println("全限定类名: " + fullClassName);
                    clazz = classLoader.loadClass(fullClassName);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if(clazz.isAnnotationPresent(Component.class)) {
                    String beanName = clazz.getAnnotation(Component.class).value();

                    BeanDefinition beanDefinition = new BeanDefinition();
                    beanDefinition.setType(clazz);
                    beanDefinition.setScope(clazz.isAnnotationPresent(Scope.class) ?
                            ((Scope)clazz.getAnnotation(Scope.class)).value() : "singleton");

                    beanDefinitionMap.put(beanName, beanDefinition);
                }
            }
        }
        // 实例化单例Bean
        for (String beanName: beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object instance = clazz.getConstructor().newInstance();
            return instance;
        } catch (InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException
                 | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if(beanDefinition == null) {
            throw new NullPointerException();
        }else {
            String scope = beanDefinition.getScope();
            if(scope.equals("singleton")) {
                // 单例
                Object bean = singletonObjects.get(beanName);
                if(bean == null) {
                    Object obj = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName, obj);
                }
                return bean;
            } else {
                // 多例
                return createBean(beanName, beanDefinition);
            }
        }
    }
}
