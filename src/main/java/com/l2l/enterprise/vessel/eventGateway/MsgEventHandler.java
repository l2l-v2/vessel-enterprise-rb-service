package com.l2l.enterprise.vessel.eventGateway;


import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.eventGateway.channel.DelayMsgChannel;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.connector.channel.AnnotationIntegrationChannels;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import com.l2l.enterprise.vessel.service.L2LTaskRuntimeImpl;
import com.l2l.enterprise.vessel.service.TestProcessRuntimeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableBinding({DelayMsgChannel.class})
public class MsgEventHandler {
    @Autowired
    MsgMatch msgMatch;
    @Autowired
    BinderAwareChannelResolver binderAwareChannelResolver;

    @Autowired
    private ShadowRepository shadowRepository;
    @StreamListener(value = DelayMsgChannel.DELAY_MSG)
    public void comfirmDelayMsg(DelayMsg delayMsg){
        Map<String, List<MsgAnnotation>> msgAnnoationMap = new HashMap<>();
        msgAnnoationMap = msgMatch.initMsgAnnotationMap();
        for(Map.Entry<String,List<MsgAnnotation>> entry : msgAnnoationMap.entrySet()){
            if(entry.getKey().equals(delayMsg.getTopic())){

                if(delayMsg.getMsgAnnotationList() == null) delayMsg.setMsgAnnotationList(new ArrayList<MsgAnnotation>());
                delayMsg.getMsgAnnotationList().addAll(entry.getValue());
            }
        }//需要根据定义id获取流程实例id
//        testProcessRuntime.processInstances(null);
        List<String> processIds = new ArrayList<>();
        if(delayMsg.getDestinationMap() ==null) delayMsg.setDestinationMap(new HashMap<>());
        for(String pid :processIds){
            String vid = shadowRepository.findRegisteredVidBypId(pid).getVid();
            delayMsg.getDestinationMap().put(vid,null);
        }
        delayMsg.setConnectorType(DelayMsgChannel.DELAY_SERVICE_CONFIRM);
        org.springframework.messaging.Message<DelayMsg> delayMsgMessage = MessageBuilder.withPayload(delayMsg).setHeader("connectorType", delayMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(delayMsg.getConnectorType()).send(delayMsgMessage);//包括topic为delay的所有msgannos
    }

    @StreamListener(value = DelayMsgChannel.DELAY_DESTINATION_UPDATE)
    public void delayDestinationUpdate(DelayMsg delayMsg){
        for(Map.Entry<String,List<Destination>>  entry : delayMsg.getDestinationMap().entrySet()){
            String vid = entry.getKey();
            String pid = shadowRepository.findRegisteredProcessesById(vid).getPid();
            //update 流程变量
        }
    }
}
