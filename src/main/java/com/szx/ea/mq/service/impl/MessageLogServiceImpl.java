package com.szx.ea.mq.service.impl;

import com.szx.core.service.AbstractMapperService;
import com.szx.ea.mq.entity.MessageLog;
import com.szx.ea.mq.mapper.MessageLogMapper;
import com.szx.ea.mq.service.MessageLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ppliu
 * created in 2018/5/24 18:04
 */
@Service
public class MessageLogServiceImpl extends AbstractMapperService<MessageLog> implements MessageLogService {

    @Autowired
    MessageLogMapper messageLogMapper;

    List<MessageLog> dataGroup;
    private volatile boolean bStopBatchThread = false;

    @Override
    public void insertNewLog(MessageLog messageLog){
        //System.out.println("into insert new log function");
        synchronized (MessageLogServiceImpl.class){
            if (dataGroup == null){
                dataGroup = new ArrayList<>();
            }

            dataGroup.add(messageLog);

            if (!batchThread.isAlive()){
                batchThread.start();
            }
        }
    }

    @Override
    public void stop(){
        bStopBatchThread = true;
    }

    private Thread batchThread = new Thread(() -> {
        while (true && !bStopBatchThread) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            batchInsert(); // 正常循环下的批量入库
        }

        // 退出线程前最后一次检查，以免落下最后一批收尾阶段进来的数据
        batchInsert();
    });

    public void batchInsert() {
        synchronized (MessageLogServiceImpl.class){
            if(dataGroup != null) {
                System.out.println("batch insert count " + Integer.toString(dataGroup.size()));
                messageLogMapper.batchInsert(new ArrayList<>(dataGroup));
                dataGroup = null;
            }
        }
    }
}
