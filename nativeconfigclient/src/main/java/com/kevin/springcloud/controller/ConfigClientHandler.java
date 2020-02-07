package com.kevin.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigClientHandler {

    @Value("${data}")
    private String data;

    @GetMapping("/data")
    public String data() {
        return data;
    }
}
