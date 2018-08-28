package com.szx.ea.mq.filter;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.szx.ea.mq.support.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class QueueMainConsumer implements Consumer {
    private static final Log logger = LogFactory.getLog(QueueMainConsumer.class);
    private ExecutorService threadPool = new ThreadPoolExecutor(0, 100, 60L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>()); // TODO 外层一次接完了，这里缓存到队列了，万一崩溃了，数据就没了。逻辑调整一下，一次接100个，即与队列大小相同

    @Autowired
    private LogFilter logFilter;

    @Autowired
    private BussinessFilter bussinessFilter;

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        MessageContext messageContext = new MessageContext();
        messageContext.setStrMqData(new String(body));
        doFilter(messageContext);
    }

    public void doFilter(MessageContext messageContext) {
        if (logFilter != null) {
            //threadPool.submit(() -> {
                try {
                    logFilter.doFilter(messageContext);
                } catch (Exception e) {
                    logger.error("fail to send msg to next");
                    e.printStackTrace();
                }
            //});
        }
    }

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

    public void init(){
        logFilter.setNextFilter(bussinessFilter);
    }
}
