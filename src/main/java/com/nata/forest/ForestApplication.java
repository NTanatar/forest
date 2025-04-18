package com.nata.forest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ForestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            var storyService = new DefaultStoryService();

            System.out.println("--- proxy one: JDK proxy mechanism ---");
            var firstProxy = createProxyOne(storyService);
            firstProxy.create();
            firstProxy.proceed();

            System.out.println("--- proxy two: spring proxy mechanism ---");
            var secondProxy = createProxyTwo(storyService);
            secondProxy.create();
            secondProxy.proceed();
        };
    }

    StoryService createProxyOne(StoryService target) {
        return (StoryService) Proxy
            .newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> wrapInvocation(target, method, args));
    }

    StoryService createProxyTwo(StoryService target) {
        var pf = new ProxyFactory();
        pf.setInterfaces(target.getClass().getInterfaces());
        pf.setTarget(target);
        pf.addAdvice((MethodInterceptor) inv -> wrapInvocation(target, inv.getMethod(), inv.getArguments()));

        return (StoryService) pf.getProxy(getClass().getClassLoader());
    }

    Object wrapInvocation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
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
