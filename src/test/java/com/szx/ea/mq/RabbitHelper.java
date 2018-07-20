package com.szx.ea.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitHelper {

    private Channel channel;
    private Connection connection;

    public boolean connect() { // TODO 做成IDE插件模板，用IDE自动生成代码
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("sgai001");
        factory.setHost("114.115.140.117");
        factory.setPort(5672);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean sendMessage(String queueName, String msg) {
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicPublish("", queueName, null, msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
