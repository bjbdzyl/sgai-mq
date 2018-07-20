package com.szx.ea.mq.service;

public abstract class AbstractBaseMessageService {
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    boolean active = true;

    public abstract void receive(Object message);

    // 本服务自定义名称，推荐用类名。注意不要与其他类重名
    public abstract String getServiceName();

    // 返回本消息处理器使用的消息class，用于框架把原始 str json 转 bean
    public abstract Class<?> getMsgObjClass();
}
