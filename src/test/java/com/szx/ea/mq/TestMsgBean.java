package com.szx.ea.mq;


import com.szx.ea.mq.support.MqHeader;

public class TestMsgBean {
    public MqHeader getHeader() {
        return header;
    }

    public void setHeader(MqHeader header) {
        this.header = header;
    }

    private MqHeader header;

    public MsgBody getBody() {
        return body;
    }

    public void setBody(MsgBody body) {
        this.body = body;
    }

    private MsgBody body;

    class MsgBody {
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        private String name;
        private String user;
    }
}
