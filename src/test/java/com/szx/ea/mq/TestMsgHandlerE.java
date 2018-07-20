package com.szx.ea.mq;

import com.szx.ea.mq.service.AbstractBaseMessageService;

public class TestMsgHandlerE extends AbstractBaseMessageService {
    @Override
    public void receive(Object msgObj) {
        TestMsgBean message = (TestMsgBean) msgObj;
        System.out.println("msg hanlder e get new message " + message.getBody().getName() + message.getBody().getUser());
    }

    @Override
    public String getServiceName() {
        return "TestMsgHandlerE";
    }

    @Override
    public Class<?> getMsgObjClass() {
        return TestMsgBean.class;
    }
}
