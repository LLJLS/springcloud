package com.kevin.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RemoteConfigClientApp {
    public static void main(String[] args) {
        SpringApplication.run(RemoteConfigClientApp.class,args);
    }
}
