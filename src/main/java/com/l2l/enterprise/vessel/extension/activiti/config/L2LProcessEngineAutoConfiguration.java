package com.l2l.enterprise.vessel.extension.activiti.config;


import com.l2l.enterprise.vessel.extension.activiti.behavior.L2LActivityBehaviorFactory;
import com.l2l.enterprise.vessel.extension.activiti.form.FormService;
import com.l2l.enterprise.vessel.extension.activiti.form.FormServiceImpl;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LServiceTaskParseHandler;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LServiceTaskXMLConverter;
import com.l2l.enterprise.vessel.extension.activiti.parser.L2LTimerDefinitionParseHandler;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.cfg.BpmnParseFactory;
import org.activiti.engine.parse.BpmnParseHandler;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({ActivitiProperties.class})
public class L2LProcessEngineAutoConfiguration extends AbstractProcessEngineAutoConfiguration {
    @Autowired
    protected UserGroupManager userGroupManager;

    public L2LProcessEngineAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {
        springAsyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(5000);
        SpringProcessEngineConfiguration conf =  this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor, this.userGroupManager);
//        conf.setBpmnParseFactory(new BpmnParseFactory() {
//            @Override
//            public BpmnParse createBpmnParse(BpmnParser bpmnParser) {
//                return new BpmnParse(conf.getBpmnParser());
//            }
//        });
        // replace the default 'ServiceTaskXMLConverter' with custom one.
        BpmnXMLConverter.addConverter(new L2LServiceTaskXMLConverter());
        // customize the default activity behavior factory
        L2LActivityBehaviorFactory l2LActivityBehaviorFactory = new L2LActivityBehaviorFactory();
        conf.setActivityBehaviorFactory(l2LActivityBehaviorFactory);
        // customize the parse handlers.
        List<BpmnParseHandler> customDefaultBpmnParseHandlers = new ArrayList<BpmnParseHandler>();
        customDefaultBpmnParseHandlers.add(new L2LServiceTaskParseHandler());
        customDefaultBpmnParseHandlers.add(new L2LTimerDefinitionParseHandler());
        conf.setCustomDefaultBpmnParseHandlers(customDefaultBpmnParseHandlers);

        // The asynchronous job executor is disabled by default and needs to be activated.
//        conf.setAsyncExecutorDefaultTimerJobAcquireWaitTime(5000);
        conf.setAsyncExecutorActivate(true);

        return conf;
    }

    @Bean
    @ConditionalOnMissingBean
    public FormService formService(SpringProcessEngineConfiguration springProcessEngineConfiguration) {
        FormService formService = new FormServiceImpl();
        if (formService instanceof ServiceImpl) {
            ((ServiceImpl)formService).setCommandExecutor(springProcessEngineConfiguration.getCommandExecutor());
        }
        return formService;
    }
}
