package com.l2l.enterprise.vessel.domain;



import com.l2l.enterprise.vessel.extension.activiti.annotation.MsgAnnotation;

import java.util.List;
import java.util.Map;

public class DelayMsg implements Msg{
    private String connectorType;
    private String delaytime;
    private List<MsgAnnotation> msgAnnotationList;
    private String topic;
    private Map<String,List<Destination>> destinationMap;
    public String getDelaytime() {
        return delaytime;
    }
    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime;
    }


    public DelayMsg(String connectorType) {
        this.connectorType = connectorType;
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
}
