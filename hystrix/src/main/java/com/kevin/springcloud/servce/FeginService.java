package com.kevin.springcloud.servce;

import com.kevin.springcloud.entity.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@FeignClient(value = "provider")
public interface FeginService {
    @GetMapping("/student/findAll")
    Collection<Student> findAll();
    @GetMapping("/student/port")
    String port();
}
