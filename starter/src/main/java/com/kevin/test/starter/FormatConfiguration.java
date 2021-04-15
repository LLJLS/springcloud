package com.kevin.test.starter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(FormatAutoConfiguration.class)
@EnableConfigurationProperties(FormatProperties.class)
@EnableAutoConfiguration
public class FormatConfiguration {

    @Bean
    public FormatTemplate formatTemplate(FormatProperties formatProperties,FormatService formatService) {
        if (formatProperties.getType().equalsIgnoreCase("json")) {
            return new FormatTemplate(new JSONService());
        }
        if (formatProperties.getType().equalsIgnoreCase("gson")) {
            return new FormatTemplate(new GSONService());
        }
        return new FormatTemplate(formatService);
    }
}
