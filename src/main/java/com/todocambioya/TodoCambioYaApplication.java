package com.todocambioya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TodoCambioYaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoCambioYaApplication.class, args);
    }
}