package com.nata.util;

import com.nata.annotation.Mystery;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.util.ReflectionUtils;

public class BeanUtils {

    public static boolean isMysterious(Object o) {
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

    public static Object createProxyOne(Object target) {
        return Proxy
            .newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (proxy, method, args) -> wrapInvocation(target, method, args));
    }

    public static Object createProxyTwo(Object target) {
        var pf = new ProxyFactory();
        pf.setInterfaces(target.getClass().getInterfaces());
        pf.setTarget(target);
        pf.addAdvice((MethodInterceptor) inv -> wrapInvocation(target, inv.getMethod(), inv.getArguments()));

        return pf.getProxy(target.getClass().getClassLoader());
    }

    public static Object wrapInvocation(Object target, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
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
