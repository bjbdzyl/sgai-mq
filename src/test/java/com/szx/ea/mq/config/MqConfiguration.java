package com.szx.ea.mq.config;


import com.szx.ea.mq.*;
import com.szx.ea.mq.service.MessageHandlersMgr;
import com.szx.ea.mq.support.SzxMqContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfiguration {

    public MqConfiguration(){
        System.out.println("mq configuration init");
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SzxMqContext getMqContext(){
        System.out.println("mq configuration init mq module");
        SzxMqContext mqContext = new SzxMqContext();
        return mqContext;
    }

    @Bean
    public MessageHandlersMgr getMgSrv(){
        System.out.println("msg handlers mgr init");
        MessageHandlersMgr msgCtrler =  new MessageHandlersMgr();
        msgCtrler.registerMessageService("typeA", new TestMsgHandlerA());
        msgCtrler.registerMessageService("typeA", new TestMsgHandlerB());
        msgCtrler.registerMessageService("typeA", new TestMsgHandlerC());
        msgCtrler.registerMessageService("typeB", new TestMsgHandlerD());
        msgCtrler.registerMessageService("typeB", new TestMsgHandlerE());
        msgCtrler.registerMessageService("typeB", new TestMsgHandlerF());
        return msgCtrler;
    }
}
