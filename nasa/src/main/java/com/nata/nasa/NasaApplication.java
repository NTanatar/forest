package com.nata.nasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NasaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NasaApplication.class, args);
    }
}
