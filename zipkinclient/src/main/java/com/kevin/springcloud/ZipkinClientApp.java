package com.kevin.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZipkinClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinClientApp.class,args);
    }
}
