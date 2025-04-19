package com.nata.riverisland;

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
    ApplicationRunner applicationRunner(ShortStoryService storyService) {
        return args -> {
            storyService.create();
            storyService.proceed();
        };
    }
}
