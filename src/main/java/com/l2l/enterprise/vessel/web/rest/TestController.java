package com.l2l.enterprise.vessel.web.rest;

import com.l2l.enterprise.vessel.extension.activiti.form.FormDefinition;
import com.l2l.enterprise.vessel.extension.activiti.form.FormService;
import com.l2l.enterprise.vessel.service.L2LTaskRuntimeImpl;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.cloud.alfresco.data.domain.AlfrescoPagedResourcesAssembler;
import org.activiti.cloud.services.core.ActivitiForbiddenException;
import org.activiti.cloud.services.core.ProcessDiagramGeneratorWrapper;
import org.activiti.cloud.services.core.pageable.SpringPageConverter;
import org.activiti.cloud.services.rest.api.ProcessDefinitionController;
import org.activiti.cloud.services.rest.api.resources.ProcessDefinitionResource;
import org.activiti.cloud.services.rest.api.resources.ProcessInstanceResource;
import org.activiti.cloud.services.rest.api.resources.TaskResource;
import org.activiti.cloud.services.rest.assemblers.ProcessInstanceResourceAssembler;
import org.activiti.cloud.services.rest.assemblers.TaskResourceAssembler;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.task.TaskQuery;
import org.activiti.image.exception.ActivitiInterchangeInfoNotFoundException;
import org.activiti.runtime.api.NotFoundException;
import org.activiti.runtime.api.ProcessRuntime;
import org.activiti.runtime.api.TaskRuntime;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.builders.TaskPayloadBuilder;
import org.activiti.runtime.api.model.impl.BPMNActivityImpl;
import org.activiti.runtime.api.model.payloads.CompleteTaskPayload;
import org.activiti.runtime.api.model.payloads.StartProcessPayload;
import org.activiti.runtime.api.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
public class TestController {
    private final RepositoryService repositoryService;
    private final ProcessDiagramGeneratorWrapper processDiagramGenerator;
    private final ProcessInstanceResourceAssembler resourceAssembler;
    private final AlfrescoPagedResourcesAssembler<ProcessInstance> pagedResourcesAssembler;
    private final ProcessRuntime processRuntime;
    private final SpringPageConverter pageConverter;
//    private final RepositoryService;
    @Autowired
    private FormService formService;
    @ExceptionHandler({ActivitiForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAppException(ActivitiForbiddenException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({ActivitiObjectNotFoundException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAppException(RuntimeException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({ActivitiInterchangeInfoNotFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String handleActivitiInterchangeInfoNotFoundException(ActivitiInterchangeInfoNotFoundException ex) {
        return ex.getMessage();
    }

    @Autowired
    public TestController(RepositoryService repositoryService, ProcessDiagramGeneratorWrapper processDiagramGenerator, ProcessInstanceResourceAssembler resourceAssembler, AlfrescoPagedResourcesAssembler<ProcessInstance> pagedResourcesAssembler, ProcessRuntime processRuntime, SpringPageConverter pageConverter) {
        this.repositoryService = repositoryService;
        this.processDiagramGenerator = processDiagramGenerator;
        this.resourceAssembler = resourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.processRuntime = processRuntime;
        this.pageConverter = pageConverter;
    }
    @RequestMapping(
        value = {"/v2/process-instances/{processDefinitionId}"},
        method = {RequestMethod.POST}
    )
    public ProcessInstanceResource testHello(@PathVariable("processDefinitionId") String processDefinitionId){
        String processDefinitionKey = "myProcess_1";
        String processInstanceName = "myProcess_1";
        StartProcessPayload startProcessPayload = new StartProcessPayload(processDefinitionId , processDefinitionKey , processInstanceName , null , null);
        return this.resourceAssembler.toResource(this.processRuntime.start(startProcessPayload));
    }

    @RequestMapping(
        value = {"/v2/process-instances/withvar/{processDefinitionId}"},
        method = {RequestMethod.POST}
    )
    public ProcessInstanceResource startProcessinstanceWithVariables(@PathVariable("processDefinitionId") String processDefinitionId ,@RequestBody Map<String,Object> varMap){
        String processDefinitionKey = "myProcess_1";
        String processInstanceName = "myProcess_1";
        StartProcessPayload startProcessPayload = new StartProcessPayload(processDefinitionId , processDefinitionKey , processInstanceName , null , varMap);
        return this.resourceAssembler.toResource(this.processRuntime.start(startProcessPayload));
    }


    @Autowired
    L2LTaskRuntimeImpl l2LTaskRuntime;
    @Autowired
    TaskResourceAssembler taskResourceAssembler;
    @RequestMapping(
        value = {"v2/{taskId}/complete"},
        method = {RequestMethod.POST}
    )
    public TaskResource completeTask(@PathVariable String taskId, @RequestBody(required = false) CompleteTaskPayload completeTaskPayload) {
        if (completeTaskPayload == null) {
            completeTaskPayload = TaskPayloadBuilder.complete().withTaskId(taskId).build();
        } else {
            completeTaskPayload.setTaskId(taskId);
        }

        Task task = this.l2LTaskRuntime.complete(completeTaskPayload);
        return this.taskResourceAssembler.toResource(task);
    }

    @RequestMapping(
        value = {"/v2/startform/{processDefinitionId}"},
        method = {RequestMethod.GET}
    )
    public FormDefinition getStartFormDefinition(@PathVariable("processDefinitionId") String processDefinitionId){
        return this.formService.getStartForm(processDefinitionId);
    }
    @Autowired
    TaskService taskService;
    @RequestMapping("/v2/gettaskform/{taskId}")
    public FormDefinition gettaskForm(@PathVariable("taskId") String taskId){
        org.activiti.engine.task.Task internalTask = (org.activiti.engine.task.Task)((TaskQuery)this.taskService.createTaskQuery().taskId(taskId)).singleResult();
        String taskDefinitionKey = internalTask.getTaskDefinitionKey();
        String processDefinitionId = internalTask.getProcessDefinitionId();
        return this.formService.getUserTaskForm(processDefinitionId,taskDefinitionKey);
    }
    @Autowired
    SenderTest senderTest;
    @RequestMapping("/sender")
    public void sender(){
        senderTest.sinkSenderTester();
    }
    @Autowired
    BpmnAdd bpmnAdd;
    @RequestMapping(
        value = {"/v2/bpmn"},
        method = {RequestMethod.POST}
    )
    public String bpmnFileChange(@RequestBody Map<String,String> fileMap) throws IOException {
        bpmnAdd.changBpmn(fileMap.get("oldfile"),fileMap.get("newfile"));
        return "ok";
    }

}
