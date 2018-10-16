package com.l2l.enterprise.vessel.config.audit;

import com.l2l.enterprise.vessel.service.TestProcessRuntimeImpl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.conf.ProcessRuntimeConfiguration;
import org.activiti.runtime.api.impl.ProcessRuntimeImpl;
import org.activiti.runtime.api.model.impl.APIProcessDefinitionConverter;
import org.activiti.runtime.api.model.impl.APIProcessInstanceConverter;
import org.activiti.runtime.api.model.impl.APIVariableInstanceConverter;
import org.activiti.spring.security.policies.ProcessSecurityPoliciesManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestProcessRuntimeAutoConfiguration {
    public TestProcessRuntimeAutoConfiguration() {
    }
    @Bean("testProcessRuntime")
    @ConditionalOnMissingBean
    public ProcessRuntime testProcessRuntime(RepositoryService repositoryService, APIProcessDefinitionConverter processDefinitionConverter, RuntimeService runtimeService, ProcessSecurityPoliciesManager securityPoliciesManager, APIProcessInstanceConverter processInstanceConverter, APIVariableInstanceConverter variableInstanceConverter, ProcessRuntimeConfiguration processRuntimeConfiguration) {
        return new TestProcessRuntimeImpl(repositoryService, processDefinitionConverter, runtimeService, securityPoliciesManager, processInstanceConverter, variableInstanceConverter, processRuntimeConfiguration);
    }
}
