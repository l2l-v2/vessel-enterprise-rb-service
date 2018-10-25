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

        Map<String , List<ExtensionElement>> extensionElements = serviceTask.getExtensionElements();
        extensionElements.forEach((key , val1)->{
            if(AnnotationConstants.ELEMENT_NAME.equals(key)){
                val1.forEach(val2->{
                    val2.getAttributes().values().forEach(extensionAttributes -> {
                        extensionAttributes.forEach(extensionAttribute -> {
                            String attrVal = extensionAttribute.getValue().trim();
                            String attrName = extensionAttribute.getName().trim();
                            if(AnnotationConstants.ATTR_POINTCUT.equals(attrName)) {
                                //add ExecutionListeners
                                if (AnnotationConstants.PRE_PROCESSOR.equals(attrVal)) {
                                    ActivitiListener activitiListener = new ActivitiListener();
                                    activitiListener.setEvent("start");
                                    activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
                                    activitiListener.setImplementation(AnnotationConstants.DELEGATE_EXPRESSION);
                                    serviceTask.getExecutionListeners().add((activitiListener));
                                } else if (AnnotationConstants.POST_PROCESSOR.equals(attrVal)) {
                                    ActivitiListener activitiListener = new ActivitiListener();
                                    activitiListener.setEvent("end");
                                    activitiListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
                                    activitiListener.setImplementation(AnnotationConstants.DELEGATE_EXPRESSION);
                                    serviceTask.getExecutionListeners().add((activitiListener));
                                } else {
                                    log.debug("unsupported type annotation executor");
                                }
                            }
                        });
                    });
                });
            }
        });

    }
}
