package com.szx.ea.mq.filter;

import com.szx.ea.mq.controller.MessageDispatcher;
import com.szx.ea.mq.support.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 调用业务逻辑处理接口，处理电文
 * @author ppliu
 * created in 2018/6/18 17:58
 */
@Service
public class BussinessFilter extends ChainFilter {
    private static final Log logger = LogFactory.getLog(BussinessFilter.class);

    @Autowired
    MessageDispatcher messageDispatcher;

    @Override
    public void doFilter(MessageContext context) {
        //logger.info("bussiness filter send msg to next");
        messageDispatcher.doHandlerMsg(context);
    }
}
