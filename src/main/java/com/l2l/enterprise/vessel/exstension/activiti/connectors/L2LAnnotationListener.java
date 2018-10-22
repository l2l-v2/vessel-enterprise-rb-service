package com.l2l.enterprise.vessel.exstension.activiti.connectors;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("l2LAnnotationListener")
public class L2LAnnotationListener implements ExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(L2LAnnotationListener.class);
    @Override
    public void notify(DelegateExecution delegateExecution) {
        String type = delegateExecution.getCurrentActivitiListener().getFieldExtensions().get(0).getStringValue();
        log.debug("---L2LAnnotationListener--- : "+type);
    }
}
