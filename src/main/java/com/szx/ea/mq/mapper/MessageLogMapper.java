package com.szx.ea.mq.mapper;

import com.szx.core.mybatis.mapper.MyBatisMapper;
import com.szx.ea.mq.entity.MessageLog;

import java.util.List;

public interface MessageLogMapper extends MyBatisMapper<MessageLog>{

    int batchInsert(List<MessageLog> logList);

    int batchUpdate(List<MessageLog> logList);
}
