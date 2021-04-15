package com.kevin.test.starter;

import com.google.gson.Gson;

public class GSONService implements FormatService{
    @Override
    public <T> String format(T t) {
        String s = new Gson().toJson(t);
        return s;
    }
}
