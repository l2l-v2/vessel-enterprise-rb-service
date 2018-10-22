package com.l2l.enterprise.vessel.extension.activiti.parser;

import org.activiti.bpmn.model.*;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class L2LServiceTaskParseHandler extends ServiceTaskParseHandler {
    private static final Logger log = LoggerFactory.getLogger(L2LServiceTaskParseHandler.class);

    protected void executeParse(BpmnParse bpmnParse, ServiceTask serviceTask) {
        log.debug("---executeParse---");
        Map<String , List<ExtensionAttribute>> attributes = serviceTask.getAttributes();
        List<ExtensionAttribute> list = attributes.get("annotation");
        log.debug("name : "+list.get(0).getName()+ " , value : "+list.get(0).getValue());
        if (StringUtils.isNotEmpty(serviceTask.getType())) {
            this.createActivityBehaviorForServiceTaskType(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_CLASS.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createClassDelegateServiceTask(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createServiceTaskDelegateExpressionActivityBehavior(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION.equalsIgnoreCase(serviceTask.getImplementationType())) {
            this.createServiceTaskExpressionActivityBehavior(bpmnParse, serviceTask);
        } else if (ImplementationType.IMPLEMENTATION_TYPE_WEBSERVICE.equalsIgnoreCase(serviceTask.getImplementationType()) && StringUtils.isNotEmpty(serviceTask.getOperationRef())) {
            this.createWebServiceActivityBehavior(bpmnParse, serviceTask);
        } else {
            this.createDefaultServiceTaskActivityBehavior(bpmnParse, serviceTask);
        }

        //add ExecutionListeners
        if(list.get(0).getValue().equals("Pre")){
            ActivitiListener activitiListener = new ActivitiListener();
            activitiListener.setEvent("start");
            List<FieldExtension> fieldExtensions = new ArrayList<FieldExtension>();
            FieldExtension field = new FieldExtension();
            field.setFieldName("Type");
            field.setStringValue("PreProcessor");
            fieldExtensions.add(field);
            activitiListener.setFieldExtensions(fieldExtensions);
            activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            activitiListener.setImplementation("${l2LAnnotationListener}");
            serviceTask.getExecutionListeners().add((activitiListener));
        }else if(list.get(0).getValue().equals("Post")){
            ActivitiListener activitiListener = new ActivitiListener();
            activitiListener.setEvent("end");
            activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
            activitiListener.setImplementation("${l2LAnnotationListener}");
            serviceTask.getExecutionListeners().add((activitiListener));
        }else{
            log.debug("unsupported type annotation executor");
        }

    }
}
