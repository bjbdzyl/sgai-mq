package com.szx.ea.mq;

import com.szx.ea.mq.service.MessageServicesManagerService;
import com.szx.ea.mq.support.SzxMqContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

// TODO 压力测试
public class AppRunListener implements SpringApplicationRunListener {

    @Autowired
    SzxMqContext szxMqContext;

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
        System.out.println("app start finished");
        SzxMqContext testBean = context.getBean(SzxMqContext.class);
        if (testBean == null) {
            System.out.println("get bean failed too. null");
        } else {
            System.out.println("wow! get bean success!!");


            testBean.start();

            MessageServicesManagerService msgCtrler = testBean.getMcs();

            // 注册
            msgCtrler.registerMessageService("typeA", new TestMsgHandlerA());
            msgCtrler.registerMessageService("typeA", new TestMsgHandlerB());
            msgCtrler.registerMessageService("typeA", new TestMsgHandlerC());
            msgCtrler.registerMessageService("typeB", new TestMsgHandlerD());
            msgCtrler.registerMessageService("typeB", new TestMsgHandlerE());
            msgCtrler.registerMessageService("typeB", new TestMsgHandlerF());
        }
        if (szxMqContext == null) {
            System.out.println("szx mq context still null. why?");
        }
    }
    // TODO 开发插件，自动生成成套的controller entity mapper serviceInterface serviceImplement
}