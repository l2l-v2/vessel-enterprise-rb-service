package com.l2l.enterprise.vessel.extension.activiti.annotation;

import com.l2l.enterprise.vessel.extension.activiti.model.Annotation;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

import java.io.Serializable;
import java.util.List;

public class GetMsgAnnotationsCmd implements Command<List<Annotation>>, Serializable {
    protected String processDefinitionId;
    public GetMsgAnnotationsCmd(String processDefinitionId){
        this.processDefinitionId = processDefinitionId;
    }
    @Override
    public List<Annotation> execute(CommandContext commandContext) {
        if(processDefinitionId == null){
            throw new ActivitiIllegalArgumentException("processDefinitionId is null");

        }else{
            Process process = ProcessDefinitionUtil.getProcess(this.processDefinitionId);
//            List<ServiceTask> serviceTasks = process.findFlowElementsOfType(Process.class);
            return  null;
        }
    }
}
