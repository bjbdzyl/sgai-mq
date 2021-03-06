package com.szx.ea.mq.support;

import com.szx.ea.mq.entity.QueueCfg;
import com.szx.ea.mq.filter.QueueMainConsumer;
import com.szx.ea.mq.service.MessageLogService;
import com.szx.ea.mq.service.QueueCfgQueryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SzxMqContext{

	private static final Log logger = LogFactory.getLog(SzxMqContext.class);

	boolean started = false;

	@Autowired
	private QueueCfgQueryService queueCfgQueryService;

	@Autowired
	MessageLogService messageLogService;

	private List<QueueCfg> queueCfgs;

	private boolean loadCfg() {
		queueCfgs = queueCfgQueryService.selectAll();
		return true;
	}

	@Autowired
	private QueueMainConsumer queueMainConsumer = new QueueMainConsumer();// 直接对接rabbitmq的queue，负责接收原始数据，然后再传递给logfiler等后续处理器

	private RabbitMqAgent rabbitMqAgent = new RabbitMqAgent();

	public boolean start(){
		logger.info("szx ctx start");
		if (!started) {
			queueMainConsumer.init();
			loadCfg();
			rabbitMqAgent.start(queueCfgs, queueMainConsumer);
		}
		return true;
	}

	public boolean stop(){
		logger.info("szx ctx stop");
		rabbitMqAgent.stop();
		messageLogService.stop(); //注意停机顺序别反，否则rabbitmq可能推来一堆新消息，我们却不入库了。
		return true;
	}

	/**
	 * @return mcs
	 */
	//public MessageHandlersMgr getMcs() {
//		return mcs;
//	}

	/**
	 * @return rabbitMqAgent
	 */
	public RabbitMqAgent getRabbitMqAgent() {
		return rabbitMqAgent;
	}
}