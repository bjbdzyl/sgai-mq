package com.szx.ea.mq;

import com.alibaba.fastjson.JSONObject;
import com.szx.ea.mq.support.MqHeader;
import org.junit.Assert;
import org.junit.Test;


public class MqMessageTest {

    @Test
    public void testToStr() {
        SimpleMessage message = new SimpleMessage();
        MqHeader header = new MqHeader();
        header.setReceiver("rec");
        header.setSender("sender");
        header.setType("q1");
        header.setTimestamp(System.currentTimeMillis());

        message.setHeader(header);
        message.setName("my name");

        System.out.println(JSONObject.toJSONString(message));
    }

    @Test
    public void testToJson() {
        String str = "{\"header\":{\"reciver\":\"rec\",\"sender\":\"sender\",\"timestamp\":1529573905136,\"type\":\"q1\"},\"name\":\"my name\"}}";
        SimpleMessage message = JSONObject.parseObject(str, SimpleMessage.class);

        String expStr = "{\"header\":{\"reciver\":\"rec\",\"sender\":\"sender\",\"timestamp\":1529573905136,\"type\":\"q1\"}}";
        Assert.assertEquals(expStr, JSONObject.toJSONString(message));
    }

    public static class SimpleMessage {
        private String name;
        private MqHeader header;

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }


        public MqHeader getHeader() {
            return header;
        }

        public void setHeader(MqHeader header) {
            this.header = header;
        }
    }
}
