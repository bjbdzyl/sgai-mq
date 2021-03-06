package com.szx.ea.mq.filter;

import com.alibaba.fastjson.JSONObject;
import com.szx.ea.mq.entity.MessageLog;
import com.szx.ea.mq.service.MessageLogService;
import com.szx.ea.mq.support.CommonMsg;
import com.szx.ea.mq.support.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * @author ppliu
 * created in 2018/6/20 20:54
 */
@Service
public class LogFilter extends ChainFilter {
	public static final String UNHANDLED = "00";
    public static final String SUCCESS = "01";
    public static final String FAILED = "02";
    private static final Log logger = LogFactory.getLog(LogFilter.class);
    @Autowired
    private MessageLogService msgLogService;

    public void doFilter(MessageContext messageContext) {
        MessageLog messageLog = createLog(messageContext.getStrMqData());

        //messageContext.setLogId(messageLog.getId()); 无ID可用，因为还没入库。
        if (next != null) {
            try {
                next.doFilter(messageContext);
                messageLog.setStatus(SUCCESS);
                //logger.info("log filter success");
            } catch (Exception e) {
                logger.fatal("log filter failed");
                messageLog.setStatus(FAILED);
                messageLog.setExceptionMessage(getStackTrace(e));
                e.printStackTrace();
            }
        } else {
            logger.error("log filter null next");
        }
        //logger.info("log filter try to update log");
        messageLog.setEndTime(new Date());
        msgLogService.insertNewLog(messageLog);
    }

    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    /**
     * 创建一条日志.
     *
     * @param content
     * @return
     */
    private MessageLog createLog(String content) {
        CommonMsg message = JSONObject.parseObject(content, CommonMsg.class);
        MessageLog messageLog = new MessageLog();
        messageLog.setStatus(UNHANDLED);
        messageLog.setReceiveTime(new Date());
        messageLog.setMessageContent(content);
        messageLog.setMessageType(message.getHeader().getType());
        messageLog.setReceiver(message.getHeader().getReceiver());
        messageLog.setSender(message.getHeader().getSender());

        return messageLog;
    }
}