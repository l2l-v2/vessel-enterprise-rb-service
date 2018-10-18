package com.l2l.enterprise.vessel.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
    "org.activiti.cloud.services",
    "org.activiti.cloud.alfresco",
    "org.activiti.spring.security.policies",
})
public class ActivitiRuntimeBundleAutoConfiguration {
    public ActivitiRuntimeBundleAutoConfiguration(){
        System.out.println("===ActivitiRuntimeBundleAutoConfiguration===");
    }
}
