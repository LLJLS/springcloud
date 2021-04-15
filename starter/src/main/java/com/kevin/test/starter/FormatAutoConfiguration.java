package com.kevin.test.starter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class FormatAutoConfiguration {

    @Bean
    @ConditionalOnClass(JSON.class)
    @Primary
    public FormatService jsonService() {
        return new JSONService();
    }

    @Bean
    @ConditionalOnClass(Gson.class)
    public FormatService gsonService() {
        return new GSONService();
    }
}
