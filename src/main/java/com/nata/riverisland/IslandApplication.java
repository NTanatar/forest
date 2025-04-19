package com.nata.riverisland;

import static com.nata.util.BeanUtils.createProxyTwo;
import static com.nata.util.BeanUtils.isMysterious;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IslandApplication {
    public static void main(String[] args) {
        SpringApplication.run(IslandApplication.class, args);
    }

        @Bean
    static MysteryBeanPostProcessor mysteryBeanPostProcessor() {
        return new MysteryBeanPostProcessor();
    }

    static class MysteryBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            if (!isMysterious(bean)) {
                return bean;
            }
            return createProxyTwo(bean);
        }
    }

    @Bean
    ApplicationRunner applicationRunner(ShortStoryService storyService) {
        return args -> {
            storyService.create();
            storyService.proceed();
        };
    }
}
