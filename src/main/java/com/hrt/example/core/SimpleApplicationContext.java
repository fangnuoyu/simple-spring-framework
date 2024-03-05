package com.hrt.example.core;

import com.hrt.example.annotation.Autowired;
import com.hrt.example.annotation.Component;
import com.hrt.example.annotation.ComponentScan;
import com.hrt.example.annotation.Scope;
import com.hrt.example.utils.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
    
    private List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public SimpleApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 扫描--->BeanDefinition--->beanDefinitionMap
        if(configClass.isAnnotationPresent(ComponentScan.class)) {
            try {
                ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
                // 包名 com.hrt.example
                String packageName = componentScanAnnotation.value();
                // 相对路径 com/hrt/example
                String relativePath  = packageName.replace(".", "/");
                ClassLoader classLoader = this.getClass().getClassLoader();
                File file = new File(classLoader.getResource(relativePath).getFile());
                // 递归遍历目录 ---> 获取com/hrt/example所有绝对路径
                List<String> fileAbsolutePathList = FileUtil.traverseDirectory(file);
                for (String fileAbsolutePath : fileAbsolutePathList) {
                    if(!fileAbsolutePath.endsWith(".class")) {
                        continue;
                    }
                    // 全限定类名 eg:
                    //      com.hrt.example.annotation.Component
                    //      com.hrt.example.annotation.ComponentScan
                    String fullClassName = fileAbsolutePath
                            .substring(fileAbsolutePath.indexOf("classes\\") + "classes\\".length())
                            .replace("\\", ".")
                            .replace(".class", "");
                    Class<?> clazz = classLoader.loadClass(fullClassName);
                    if(clazz.isAnnotationPresent(Component.class)) {
                        if(BeanPostProcessor.class.isAssignableFrom(clazz)) {
                            BeanPostProcessor beanPostProcessor = (BeanPostProcessor)clazz.newInstance();
                            beanPostProcessorList.add(beanPostProcessor);
                        }

                        String beanName = clazz.getAnnotation(Component.class).value();
                        if(StringUtils.isBlank(beanName)) {
                            beanName = Introspector.decapitalize(clazz.getSimpleName());
                        }

                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setType(clazz);
                        beanDefinition.setScope(clazz.isAnnotationPresent(Scope.class) ?
                                ((Scope)clazz.getAnnotation(Scope.class)).value() : "singleton");

                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
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
            // 依赖注入
            for (Field f : clazz.getDeclaredFields()) {
                if(f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true);
                    f.set(instance, getBean(f.getName()));
                }
            }
            // Aware回调
            if(instance instanceof BeanNameAware) {
                ((BeanNameAware)instance).setBeanName(beanName);
            }
            // 初始化前
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }
            // 初始化
            if(instance instanceof InitializingBean) {
                ((InitializingBean)instance).afterPropertiesSet();
            }
            // 初始化后
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

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
