package com.szx.ea.mq;

import com.szx.ea.mq.service.AbstractBaseMessageService;

public class TestMsgHandlerC extends AbstractBaseMessageService {
    @Override
    public void receive(Object msgObj) {
        TestMsgBean message = (TestMsgBean) msgObj;
        System.out.println("msg hanlder c get new message " + message.getBody().getName() + message.getBody().getUser());
    }

    @Override
    public String getServiceName() {
        return "TestMsgHandlerC";
    }

    @Override
    public Class<?> getMsgObjClass() {
        return TestMsgBean.class;
    }
}
