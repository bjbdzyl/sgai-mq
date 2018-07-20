package com.szx.ea.mq;

import com.szx.ea.mq.config.MqConfiguration;
import com.szx.ea.mq.service.MessageServicesManagerService;
import com.szx.ea.mq.support.RabbitMqAgent;
import com.szx.ea.mq.support.SzxMqContext;
import junit.runner.BaseTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MqApplicationTests.class)
@ComponentScan(basePackages = {"com.szx.ea.mq.config"})
public class MqApplicationTests {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        System.out.println("before test");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void afterTest() {
        System.out.println("after test");
    }

    @Autowired
    MqConfiguration mqConfiguration;

    @Test
    public void contextLoads() {
        System.out.println("this is my spring test");
    }

    @Test
    public void SzxMqContextTest() {
        System.out.println("start szx mq context");

        SzxMqContext testBean = mqConfiguration.startMqModule();
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

    @Test
    public void webApiTest() {
        SzxMqContextTest();
        try {
            RabbitHelper rabbitHelper = new RabbitHelper();
            System.out.println("before stop queue. try send msg");
            rabbitHelper.connect();
            rabbitHelper.sendMessage("queue1", "{\"header\":{\"receiver\":\"before\",\"sender\":\"sender\",\"timestamp\":1529573905136,\"type\":\"typeA\"},\"body\":{\"name\":\"test name\", \"user\":\"test user\"}}");
            System.out.println("end send msg before stop queue");
            mockMvc.perform(MockMvcRequestBuilders.post("/api/mq/set_queue_state?ip=114.115.140.117&port=5672&queue=queue1&active=0"))
                    .andDo(MockMvcResultHandlers.print());

            System.out.println("after stop queue. try send msg");
            rabbitHelper.sendMessage("queue1", "{\"header\":{\"receiver\":\"after\",\"sender\":\"sender\",\"timestamp\":1529573905136,\"type\":\"typeA\"},\"body\":{\"name\":\"test name\", \"user\":\"test user\"}}");
            System.out.println("end send msg after stop queue");
        } catch (Exception e) {
            System.out.println("mock mvc perform test get exception");
            e.printStackTrace();
        }
    }
}
