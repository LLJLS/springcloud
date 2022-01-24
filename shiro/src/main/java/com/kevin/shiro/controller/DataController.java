package com.kevin.shiro.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    @RequestMapping("/data/add")
    public String add() {
        return "这是新增页面";
    }

    @RequestMapping("/data/del")
    public String del() {
        return "这是删除页面";
    }

    @RequestMapping("/data/update")
    public String update() {
        return "这是修改页面";
    }

    @RequestMapping("/data/list")
    public String list() {
        return "这是展示页面";
    }
}
