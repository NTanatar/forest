package com.nata.forest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;

@SpringBootApplication
public class ForestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestApplication.class, args);
    }

    static boolean isMysterious(Object o) {
        var isMysterious = new AtomicBoolean(false);
        var classes = new ArrayList<Class<?>>();
        classes.add(o.getClass());
        Collections.addAll(classes, o.getClass().getInterfaces());
        classes.forEach(cl -> ReflectionUtils.doWithMethods(cl, method -> {
            if (method.getAnnotation(Mystery.class) != null) {
                isMysterious.set(true);
            }
        }));
        return isMysterious.get();
    }

    @Bean
    static MysteryBeanPostProcessor mysteryBeanPostProcessor() {
        return new MysteryBeanPostProcessor();
    }

    static class MysteryBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            if (isMysterious(bean)) {
                return createProxyTwo(bean);
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        }
    }

    @Bean
    public DefaultStoryService storyService() {
        return new DefaultStoryService();
    }

    @Bean
    ApplicationRunner applicationRunner(StoryService storyService) {
        return args -> {
           storyService.create();
           storyService.proceed();
        };
    }

    static Object createProxyOne(Object target) {
        return Proxy
            .newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> wrapInvocation(target, method, args));
    }

    static Object createProxyTwo(Object target) {
        var pf = new ProxyFactory();
        pf.setInterfaces(target.getClass().getInterfaces());
        pf.setTarget(target);
        pf.addAdvice((MethodInterceptor) inv -> wrapInvocation(target, inv.getMethod(), inv.getArguments()));

        return pf.getProxy(target.getClass().getClassLoader());
    }

    static Object wrapInvocation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (method.getAnnotation(Mystery.class) != null) {
            System.out.println("mysterious things starting");
        }
        try {
            return method.invoke(target, args);

        } finally {
            if (method.getAnnotation(Mystery.class) != null) {
                System.out.println("mysterious things ending");
            }
        }
    }
}
