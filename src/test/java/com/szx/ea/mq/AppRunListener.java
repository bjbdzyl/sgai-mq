package com.szx.ea.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

// TODO 压力测试
public class AppRunListener implements SpringApplicationRunListener {
    public AppRunListener(SpringApplication app, String[] args) {
        System.out.println("app run listener init");
    }

    @Override
    public void starting() {
        System.out.println("app starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("app env prepared");
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("app ctx prepared");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("app ctx loaded");
    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }


    // TODO 开发插件，自动生成成套的controller entity mapper serviceInterface serviceImplement
}