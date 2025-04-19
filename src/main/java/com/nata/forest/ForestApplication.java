package com.nata.forest;

import static com.nata.util.BeanUtils.createProxyTwo;
import static com.nata.util.BeanUtils.isMysterious;

import org.springframework.beans.factory.config.BeanPostProcessor;
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
    ApplicationRunner applicationRunner(StoryService storyService) {
        return args -> {
           storyService.create();
           storyService.proceed();
        };
    }
}
