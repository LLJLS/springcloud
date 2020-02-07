package com.kevin.springcloud.controller;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.FeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/fegin")
public class FeginHandler {

    @Autowired
    FeginService feginService;

    @GetMapping("/findAll")
    public Collection<Student> findAll(){
        return feginService.findAll();
    }

    @GetMapping("/port")
    public String port() {
        return feginService.port();
    }


}
