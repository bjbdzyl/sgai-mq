/**
 * 
 */
package com.szx.ea.mq.service;

import com.szx.ea.mq.controller.MessageDispatcher;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Administrator
 *
 */
public class MessageHandlersMgr {

	public class MsgSrvList extends ArrayList<AbstractBaseMessageService> {}

	private Map<String, MsgSrvList> asyncMsgSrvMap = new ConcurrentHashMap();

	public MsgSrvList getMsgHandlerList(String msgType){
		MsgSrvList handlers;
		synchronized (MessageHandlersMgr.class) {
			handlers = asyncMsgSrvMap.get(msgType);
		}
		return handlers;
	}

	public void unregisterHandlerService(String msgType, String serviceName){
		System.out.println("before unregister srv list size is " + Integer.toString(asyncMsgSrvMap.get(msgType).size()));
		synchronized (MessageDispatcher.class) {
			if (asyncMsgSrvMap.containsKey(msgType)) {
				MsgSrvList services = asyncMsgSrvMap.get(msgType);
				for (int i = 0; i < services.size(); i++) {
					if (serviceName.contentEquals(services.get(i).getServiceName())) {
						services.remove(i);
						System.out.println("after unregister srv list size is " + Integer.toString(asyncMsgSrvMap.get(msgType).size()));
						return ;
					}
				}
			}
		}
		return ;
	}

	public void registerMessageService(String msgType, AbstractBaseMessageService srvInstance){
		synchronized (MessageDispatcher.class){
			if(asyncMsgSrvMap.containsKey(msgType)) {
				asyncMsgSrvMap.get(msgType).add(srvInstance);
			} else {
				MsgSrvList services = new MsgSrvList();
				services.add(srvInstance);
				asyncMsgSrvMap.put(msgType, services);
			}
		}
		return ;
	}

	public void unregisterMessageService(String msgType, AbstractBaseMessageService srvInstance){
		synchronized (MessageDispatcher.class) {
			if (asyncMsgSrvMap.containsKey(msgType)) {
				asyncMsgSrvMap.get(msgType).remove(srvInstance);
				return ;
			}
		}
		return ;
	}

//
//	public boolean pauseMessageService(String msgType, String serviceName){
//		msgCtrl.setMessageServiceState(msgType, serviceName, false);
//		return true;
//	}
//
//	public boolean resumeMessageService(String msgType, String serviceName){
//		msgCtrl.setMessageServiceState(msgType, serviceName, true);
//		return true;
//	}
//
//    public List<String> getMsgSrvList(String msgType) {
//        return msgCtrl.getMsgSrvList(msgType);
//    }
}
