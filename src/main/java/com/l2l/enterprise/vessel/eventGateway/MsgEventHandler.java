package com.l2l.enterprise.vessel.eventGateway;


import afu.org.checkerframework.checker.units.qual.A;
import com.l2l.enterprise.vessel.domain.DelayMsg;
import com.l2l.enterprise.vessel.domain.Destination;
import com.l2l.enterprise.vessel.eventGateway.channel.DelayMsgChannel;
import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;
import com.l2l.enterprise.vessel.extension.activiti.boot.L2LProcessEngineConfiguration;
import com.l2l.enterprise.vessel.extension.activiti.connector.channel.AnnotationIntegrationChannels;
import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import com.l2l.enterprise.vessel.repository.ShadowRepository;
import com.l2l.enterprise.vessel.service.L2LTaskRuntimeImpl;
import com.l2l.enterprise.vessel.service.TestProcessRuntimeImpl;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
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
    VidPidRegistry vidPidRegistry;
    @Autowired
    L2LProcessEngineConfiguration l2LProcessEngineConfiguration;
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
        RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
        for(MsgAnnotation msgAnnotation : delayMsg.getMsgAnnotationList()){
            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId(msgAnnotation.getProcessDefinitionId()).list();
            if(delayMsg.getDestinationMap() ==null) delayMsg.setDestinationMap(new HashMap<>());
            for(ProcessInstance processInstance :processInstances){
                String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
                List<Destination> destinations = (List<Destination>) runtimeService.getVariable(processInstance.getId(),"destation");//读取变量需要测试
                delayMsg.getDestinationMap().put(vid,destinations);
            }
        }
//        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processDefinitionId("id").list();
//        if(delayMsg.getDestinationMap() ==null) delayMsg.setDestinationMap(new HashMap<>());
//        for(ProcessInstance processInstance :processInstances){
//            String vid = vidPidRegistry.findRegisteredVidBypId(processInstance.getId());
//            delayMsg.getDestinationMap().put(vid,null);
//        }
        delayMsg.setConnectorType(DelayMsgChannel.DELAY_SERVICE_CONFIRM);
        org.springframework.messaging.Message<DelayMsg> delayMsgMessage = MessageBuilder.withPayload(delayMsg).setHeader("connectorType", delayMsg.getConnectorType()).build();
        binderAwareChannelResolver.resolveDestination(delayMsg.getConnectorType()).send(delayMsgMessage);//包括topic为delay的所有msgannos
    }

    @StreamListener(value = DelayMsgChannel.DELAY_DESTINATION_UPDATE)
    public void delayDestinationUpdate(DelayMsg delayMsg){
        for(Map.Entry<String,List<Destination>>  entry : delayMsg.getDestinationMap().entrySet()){
            String vid = entry.getKey();
            String pid = vidPidRegistry.findRegisteredpidByvId(vid);
            //update 流程变量v
            RuntimeService runtimeService = l2LProcessEngineConfiguration.getRuntimeService();
            runtimeService.setVariable(pid,"destation",entry.getValue());
//            for(Destination destination : entry.getValue()){
//                runtimeService.setVariable(pid,destination.getName() +"estiAnchorTime",destination.getEstiAnchorTime());
//                runtimeService.setVariable(pid,destination.getName() +"estiArrivalTime",destination.getEstiArrivalTime());
//                runtimeService.setVariable(pid,destination.getName() +"estiDepartureTime",destination.getEstiDepartureTime());
//            }//赋值完成 变量名称待变

        }
    }
}
