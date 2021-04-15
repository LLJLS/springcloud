package com.kevin.test.starter;

public class FormatTemplate {

    private FormatService formatService;

    public FormatTemplate(FormatService formatService) {
        this.formatService = formatService;
    }

    public <T> String fomate(T t) {
        return formatService.format(t);
    }
}
