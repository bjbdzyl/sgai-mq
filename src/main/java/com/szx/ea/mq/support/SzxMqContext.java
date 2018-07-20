package com.szx.ea.mq.support;

import com.szx.ea.mq.entity.QueueCfg;
import com.szx.ea.mq.filter.QueueMainConsumer;
import com.szx.ea.mq.service.MessageServicesManagerService;
import com.szx.ea.mq.service.QueueCfgQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class SzxMqContext{

	@Autowired
	private QueueCfgQueryService queueCfgQueryService;

	private List<QueueCfg> queueCfgs;

	private boolean loadCfg() {
		queueCfgs = queueCfgQueryService.selectAll();
		return true;
	}

	@Autowired
	private MessageServicesManagerService mcs;// = new MessageControllerService();

	@Autowired
	private QueueMainConsumer queueMainConsumer;// 直接对接rabbitmq的queue，负责接收原始数据，然后再传递给logfiler等后续处理器

	private RabbitMqAgent rabbitMqAgent = new RabbitMqAgent();

	public boolean start(){
		queueMainConsumer.init();
		loadCfg();
		rabbitMqAgent.start(queueCfgs, queueMainConsumer);
		return true;
	}

	// TODO 交给MqConfiguration调用？
	public boolean stop(){
		rabbitMqAgent.stop();
		return true;
	}

	/**
	 * @return mcs
	 */
	public MessageServicesManagerService getMcs() {
		return mcs;
	}

	/**
	 * @return rabbitMqAgent
	 */
	public RabbitMqAgent getRabbitMqAgent() {
		return rabbitMqAgent;
	}
}