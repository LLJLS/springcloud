package com.kevin.springcloud.service.impl;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.FeginService;
import org.springframework.stereotype.Component;

import java.util.Collection;


@Component
public class FeginError implements FeginService {
    @Override
    public Collection<Student> findAll() {
        return null;
    }

    @Override
    public String port() {
        return "服务维护中。。。";
    }
}
