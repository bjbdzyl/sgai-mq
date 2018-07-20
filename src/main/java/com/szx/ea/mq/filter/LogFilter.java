package com.szx.ea.mq.filter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.szx.ea.mq.service.MessageLogService;
import com.szx.ea.mq.support.CommonMsg;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.szx.ea.mq.entity.MessageLog;
import com.szx.ea.mq.support.MessageContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

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
    private MessageLogService msgLogService;// = new MessageLogServiceImpl();

    @Override
    public void doFilter(MessageContext messageContext) {
        MessageLog messageLog = createLog(messageContext.getStrMqData());
        msgLogService.insert(messageLog);
        messageContext.setLogId(messageLog.getId()); // 如果后续要更新messagelog，就不得不读写库，性能会受影响
        if (next != null) {
            try {
                next.doFilter(messageContext);
                messageLog.setStatus(SUCCESS);
                logger.info("log filter success");
            } catch (Exception e) {
                logger.fatal("log filter failed");
                messageLog.setStatus(FAILED);
                messageLog.setExceptionMessage(getStackTrace(e));
                e.printStackTrace();
            }
        } else {
            logger.error("log filter null next");
        }
        logger.info("log filter try to update log");
        messageLog.setEndTime(new Date());
        msgLogService.updateByPrimaryKey(messageLog);
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

// TODO 精简autowired
// TODO 进一步明确区哪些是controller，哪些是Service，规范和正确的使用注解，避免出现容器管理上的浪费和BUG