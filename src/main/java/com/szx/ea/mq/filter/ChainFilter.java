package com.szx.ea.mq.filter;


import com.szx.ea.mq.support.MessageContext;

/**
 * 消息过滤器.
 * @author ppliu
 * created in 2018/6/18 17:21
 */
public abstract class ChainFilter  {
	
    protected ChainFilter next;
    
    public ChainFilter setNextFilter(ChainFilter next) {
        this.next = next;
        return next;
    }

    public  abstract void doFilter(MessageContext messageContext);
}
