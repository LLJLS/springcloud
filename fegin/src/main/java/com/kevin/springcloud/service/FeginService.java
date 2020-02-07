package com.kevin.springcloud.service;

import com.kevin.springcloud.entity.Student;
import com.kevin.springcloud.service.impl.FeginError;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "provider",fallback = FeginError.class)
public interface FeginService {
    @GetMapping("/student/findAll")
    public Collection<Student> findAll();
    @GetMapping("/student/port")
    public String port();
}
