package com.kevin.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @RequestMapping("/security/add")
    public String add() {
        return "这是新增页面";
    }

    @RequestMapping("/security/list")
    public String list() {
        return "这是展示页面";
    }

    @RequestMapping("/security/update")
    public String update() {
        return "这是更新页面";
    }

    @RequestMapping("/security/del")
    public String del() {
        return "这是删除页面";
    }
}
