package com.sanyi.sanyiopc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SanyiopcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SanyiopcApplication.class, args);
    }

}
