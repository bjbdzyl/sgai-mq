/**
 * 
 */
package com.szx.ea.mq.service;

import com.szx.ea.mq.support.MessageContext;
import com.szx.ea.mq.controller.MessageServicesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author Administrator
 *
 */
@Controller
public class MessageServicesManagerService {

	@Autowired
	private MessageServicesManager msgCtrl;

	// TODO 配置里支持多服务器，这里注册监听却没要求指定服务器，矛盾啊！下同。
	// TODO 讨论一下，需要统一，即组件是否需要支持多服务器？
	public void registerMessageService(String msgType, AbstractBaseMessageService service){
		msgCtrl.registerHandlerService(msgType, service);
	}


	public void unregisterMessageService(String msgType, AbstractBaseMessageService service){
		msgCtrl.unregisterHandlerService(msgType, service);
	}

	public boolean pauseMessageService(String msgType, String serviceName){
		msgCtrl.setMessageServiceState(msgType, serviceName, false);
		return true;
	}

	public boolean resumeMessageService(String msgType, String serviceName){
		msgCtrl.setMessageServiceState(msgType, serviceName, true);
		return true;
	}

    public List<String> getMsgSrvList(String msgType) {
        return msgCtrl.getMsgSrvList(msgType);
    }
}
