package com.szx.ea.mq.support;


public class CommonMsg {
    public MqHeader getHeader() {
        return header;
    }

    public void setHeader(MqHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private MqHeader header;
    private String body;
}
