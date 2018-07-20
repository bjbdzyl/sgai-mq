package com.szx.ea.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

//@EnableWebMvc
@SpringBootApplication
//@ServletComponentScan(basePackages = {"com.szx.ea.mq"})
//@EnableSwagger2
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}