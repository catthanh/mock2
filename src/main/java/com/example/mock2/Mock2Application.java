package com.example.mock2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Mock2Application {

    public static void main(String[] args) {
        SpringApplication.run(Mock2Application.class, args);
    }

}
