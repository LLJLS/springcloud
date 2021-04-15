package com.kevin.test.starter;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = FormatProperties.TYPE)
public class FormatProperties {

    public static final String TYPE = "format";

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
