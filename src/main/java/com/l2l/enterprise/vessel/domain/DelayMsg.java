package com.l2l.enterprise.vessel.domain;



import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;

import java.util.List;
import java.util.Map;

public class DelayMsg implements Msg{
    private List<String> pids;//delete
    private String connectorType;
    private String delayx;
    private String delayy;
    private List<MsgAnnotation> msgAnnotationList;
    private String topic;
    private Map<String,List<Destination>> destinationMap;


    public DelayMsg(String connectorType) {
        this.connectorType = connectorType;
    }

    public List<String> getPids() {
        return pids;
    }

    public void setPids(List<String> pids) {
        this.pids = pids;
    }

    public String getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<MsgAnnotation> getMsgAnnotationList() {
        return msgAnnotationList;
    }

    public void setMsgAnnotationList(List<MsgAnnotation> msgAnnotationList) {
        this.msgAnnotationList = msgAnnotationList;
    }

    public Map<String, List<Destination>> getDestinationMap() {
        return destinationMap;
    }

    public void setDestinationMap(Map<String, List<Destination>> destinationMap) {
        this.destinationMap = destinationMap;
    }

    public String getDelayx() {
        return delayx;
    }

    public void setDelayx(String delayx) {
        this.delayx = delayx;
    }

    public String getDelayy() {
        return delayy;
    }

    public void setDelayy(String delayy) {
        this.delayy = delayy;
    }
}
