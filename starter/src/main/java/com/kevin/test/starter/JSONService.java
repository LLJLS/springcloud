package com.kevin.test.starter;

import com.alibaba.fastjson.JSON;

public class JSONService implements FormatService{
    @Override
    public <T> String format(T t) {
        return JSON.toJSONString(t);
    }
}
