package com.nata.forest;

import java.lang.reflect.Proxy;
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

            // proxy example one (JDK proxy mechanism):
            var firstProxy = createProxy(storyService);
            firstProxy.create();
            firstProxy.proceed();

        };
    }

    private StoryService createProxy(StoryService storyService) {
        return (StoryService) Proxy.newProxyInstance(
            storyService.getClass().getClassLoader(),
            storyService.getClass().getInterfaces(),
            (proxy, method, args1) -> {
                if (method.getAnnotation(Mystery.class) != null) {
                    System.out.println("mysterious things starting");
                }
                try {
                    return method.invoke(storyService, args1);

                } finally {
                    if (method.getAnnotation(Mystery.class) != null) {
                        System.out.println("mysterious things ending");
                    }
                }
            });
    }
}
