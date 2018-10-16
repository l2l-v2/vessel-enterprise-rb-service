package com.l2l.enterprise.vessel.web.rest;

import org.activiti.cloud.services.rest.api.ProcessDefinitionController;
import org.activiti.cloud.services.rest.api.resources.ProcessDefinitionResource;
import org.activiti.runtime.api.model.ProcessDefinition;
import org.activiti.runtime.api.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public ResponseEntity<String> testHello(){
        String str = "Hello Gateway!!";
        System.out.println(str);
        return new ResponseEntity<String>(str , HttpStatus.OK);
    }

}
