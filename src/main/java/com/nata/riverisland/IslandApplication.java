package com.nata.riverisland;

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

    static class MysteriousShortStoryService extends ShortStoryService {
        @Override
        public void create() {
            System.out.println("It is foggy");
            super.create();
            System.out.println("Fog disappeared");
        }
    }

    @Bean
    static MysteryBeanPostProcessor mysteryBeanPostProcessor() {
        return new MysteryBeanPostProcessor();
    }

    static class MysteryBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) {
            if (bean instanceof ShortStoryService) {
                return new MysteriousShortStoryService();
            }
            return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
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
