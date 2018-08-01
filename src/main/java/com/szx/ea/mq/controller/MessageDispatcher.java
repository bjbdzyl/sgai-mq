package com.szx.ea.mq.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.szx.ea.mq.service.AbstractBaseMessageService;
import com.szx.ea.mq.service.MessageHandlersMgr;
import com.szx.ea.mq.support.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// TODO EventBus? msg type 的数据类型怎么办？统一CommonMsg？
// 若用event bus ，不便监控每个消息处理器的处理结果
@Controller
public class MessageDispatcher { //TODO 和service倒过来依赖。

    @Autowired
    MessageHandlersMgr messageHandlersMgr;

    private static final Log logger = LogFactory.getLog(MessageDispatcher.class);

    private ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

    public void doHandlerMsg(MessageContext msgCtx){
    	MessageHandlersMgr.MsgSrvList handlers = messageHandlersMgr.getMsgHandlerList(msgCtx.getMessageType());
    	if(handlers != null) {
    		for (AbstractBaseMessageService messageService : handlers) {
    		    if (messageService.isActive()) {
                    threadPool.submit(() -> {
                        try {
                            if (messageService != null) {
                                messageService.receive(JSONObject.parseObject(msgCtx.getStrMqData(), messageService.getMsgObjClass()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
			}
    	} else {
    	    System.out.println("msg handlers null");
        }
    }
}
