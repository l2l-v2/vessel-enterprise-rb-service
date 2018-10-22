package com.l2l.enterprise.vessel.config;

import com.l2l.enterprise.vessel.service.ListObjects;
import com.l2l.enterprise.vessel.service.TestListObject;
import org.activiti.spring.boot.EndpointAutoConfiguration;
import org.activiti.spring.boot.ProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.util.List;

@Configuration
@ComponentScan(
    basePackages = {"org.activiti"} ,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = ProcessEngineAutoConfiguration.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE , value = EndpointAutoConfiguration.class)

    }
 )
public class L2LActivitiRuntimeBundleAutoConfiguration {
    public L2LActivitiRuntimeBundleAutoConfiguration(){
        System.out.println("===ActivitiRuntimeBundleAutoConfiguration===");
    }

    @Bean
    public TestListObject testListObject(){
        return new TestListObject();
    }
    @Bean
    public TestListObject testListObject1(){
        return new TestListObject();
    }


    @Bean
    public ListObjects listObjects(@Autowired(required = false) List<TestListObject> listObjects){
        return new ListObjects(listObjects);
    }
}
