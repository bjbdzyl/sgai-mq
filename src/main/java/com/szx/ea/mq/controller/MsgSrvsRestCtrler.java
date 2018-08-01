package com.szx.ea.mq.controller;

import com.szx.core.jwt.annotation.PermessionLimit;
import com.szx.ea.mq.support.SzxMqContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/mq")
public class MsgSrvsRestCtrler {

    @Autowired
    private SzxMqContext szxMqContext;

    private static final Log logger = LogFactory.getLog(MsgSrvsRestCtrler.class);

//    @PermessionLimit(limit = false)
//    @RequestMapping(value = "/query_srv_list", method = RequestMethod.GET)
//    public List<String> getMsgServiceMap(@RequestParam("msgType") String msgType){
//        logger.info("into web api query srv list for type " + msgType);
//        return szxMqContext.getMcs().getMsgSrvList(msgType);
//    }
//
//    @PermessionLimit(limit = false)
//    @RequestMapping(value = "/set_srv_state", method = RequestMethod.POST)
//    public boolean setMsgServiceState(@RequestParam("msgType") String msgType,
//                                    @RequestParam("service_name") String serviceName,
//                                    @RequestParam("active") int active){
//        logger.info("into web api set msg srv state for " + msgType + " " + serviceName + " " + Integer.toString(active));
//        if (active == 1){
//            return szxMqContext.getMcs().resumeMessageService(msgType, serviceName);
//        }
//        else if (active == 0){
//            return szxMqContext.getMcs().pauseMessageService(msgType, serviceName);
//        }
//        else {
//            logger.warn("warning!!! set srv state api get an invalid http param");
//            return false;
//        }
//    }

    @PermessionLimit(limit = false)
    @RequestMapping(value = "/set_queue_state", method = RequestMethod.POST)
    public boolean setQueueConsumersState(@RequestParam("ip") String mqSrvIp, @RequestParam("port") int port, @RequestParam("queue") String queueName, @RequestParam("active") int active){
        logger.info("into web api set queue state for queue " + queueName + " active " + Integer.toString(active) + " in server " + mqSrvIp + " at port " + Integer.toString(port));
        return szxMqContext.getRabbitMqAgent().setQueueState(mqSrvIp, port, queueName, active);
    }
}
