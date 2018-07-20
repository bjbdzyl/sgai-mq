package com.szx.ea.mq.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.szx.ea.mq.service.AbstractBaseMessageService;
import com.szx.ea.mq.support.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;

// TODO 组件内各种Exception处理
@Controller
public class MessageServicesManager {

    // private static final Log logger = LogFactory.getLog(MessageServicesManager.class);

    class MsgSrvList extends ArrayList<AbstractBaseMessageService> {}

    private Map<String, MsgSrvList> asyncMsgSrvMap = new ConcurrentHashMap();
    
    private ExecutorService threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());

    public boolean registerHandlerService(String msgType, AbstractBaseMessageService srvInstance){
    	synchronized (MessageServicesManager.class){
			if(asyncMsgSrvMap.containsKey(msgType)) {
				asyncMsgSrvMap.get(msgType).add(srvInstance);
			} else {
				MsgSrvList services = new MsgSrvList();
				services.add(srvInstance);
				asyncMsgSrvMap.put(msgType, services);
			}
		}
    	return true;
    }

    public boolean unregisterHandlerService(String msgType, AbstractBaseMessageService srvInstance){
    	synchronized (MessageServicesManager.class) {
			if (asyncMsgSrvMap.containsKey(msgType)) {
				asyncMsgSrvMap.get(msgType).remove(srvInstance);
				return true;
			}
		}
		return false;
    }

    public boolean unregisterHandlerService(String msgType, String serviceName){
        System.out.println("before unregister srv list size is " + Integer.toString(asyncMsgSrvMap.get(msgType).size()));
        synchronized (MessageServicesManager.class) {
            if (asyncMsgSrvMap.containsKey(msgType)) {
                MsgSrvList services = asyncMsgSrvMap.get(msgType);
                for (int i = 0; i < services.size(); i++) {
                    if (serviceName.contentEquals(services.get(i).getServiceName())) {
                        services.remove(i);
                        System.out.println("after unregister srv list size is " + Integer.toString(asyncMsgSrvMap.get(msgType).size()));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean setMessageServiceState(String msgType, String serviceName, boolean active){
        synchronized (MessageServicesManager.class) {
            MsgSrvList serviceList = asyncMsgSrvMap.get(msgType);
            if (serviceList != null) {
                for (int i = 0; i < serviceList.size(); i++) {
                    if (serviceList.get(i).getServiceName().compareTo(serviceName) == 0) {
                        serviceList.get(i).setActive(active);
                    }
                }
            }
        }
        return true;
	}

    public void doHandlerMsg(MessageContext msgCtx){
    	MsgSrvList handlers;
    	System.out.println("msg handlers get msg type " + msgCtx.getMessageType());
    	synchronized (MessageServicesManager.class) {
            handlers = asyncMsgSrvMap.get(msgCtx.getMessageType());
        }
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

	public List<String> getMsgSrvList(String msgType) {
		List<String> ret = new ArrayList<>();
        MsgSrvList srvList;
		synchronized (MessageServicesManager.class) {
            srvList = asyncMsgSrvMap.get(msgType);
        }

		if (srvList != null){
			for (AbstractBaseMessageService srvInstance :
					srvList) {
				ret.add(srvInstance.getServiceName());
			}
			return ret;
		}

		return null;
	}

    public boolean getMsgServiceState(String msgType, String serviceName) {
        synchronized (MessageServicesManager.class) {
            if (asyncMsgSrvMap.containsKey(msgType)) {
                MsgSrvList services = asyncMsgSrvMap.get(msgType);
                for (int i = 0; i < services.size(); i++) {
                    if (serviceName.contentEquals(services.get(i).getServiceName())) {
                        return services.get(i).isActive();
                    }
                }
            }
        }
        return false;
    }
}
