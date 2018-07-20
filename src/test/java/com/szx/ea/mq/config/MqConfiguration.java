package com.szx.ea.mq.config;


import com.szx.ea.mq.support.SzxMqContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfiguration {

    @Bean
    public SzxMqContext startMqModule(){
        System.out.println("mq configuration start mq module");
        SzxMqContext mqContext = new SzxMqContext();
        //mqContext.start();
        return mqContext;
    }
}
