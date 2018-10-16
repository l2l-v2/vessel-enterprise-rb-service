package com.l2l.enterprise.vessel.activiti;

import com.l2l.enterprise.vessel.VesselApp;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Tests for  Activiti engine API
 * @bqzhu
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VesselApp.class)
@ActiveProfiles("dev")
public class ActivitiUnitTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;
    @Test
    public void deploy(){
        List<ProcessDefinition> pds = repositoryService.createProcessDefinitionQuery().list();
        System.out.println("pds size = "+pds.size());
        pds.forEach(pd->{
            System.out.println(pd.toString());
        });
        System.out.println("just for test"+runtimeService);
    }
}
