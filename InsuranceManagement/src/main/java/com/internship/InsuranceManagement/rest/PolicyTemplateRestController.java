package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.PolicyTemplate;
import com.internship.InsuranceManagement.service.interfaces.PolicyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policy_template/")
public class PolicyTemplateRestController {

    private final PolicyTemplateService templateService;

    @Autowired
    public PolicyTemplateRestController(PolicyTemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping
    public List<PolicyTemplate> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @GetMapping("/{id}")
    public PolicyTemplate getTemplateById(@PathVariable int id) {
        return templateService.getTemplateById(id);
    }

    @GetMapping("/agent/{agentId}")
    public List<PolicyTemplate> getTemplatesByAgent(@PathVariable int agentId) {
        return templateService.getTemplatesByAgentId(agentId);
    }

    @PostMapping("/")
    public PolicyTemplate createTemplate(@RequestBody PolicyTemplate template) {
        return templateService.createOrUpdateTemplate(template);
    }

    @PutMapping("/{id}")
    public PolicyTemplate updateTemplate(@PathVariable int id, @RequestBody PolicyTemplate template) {
        template.setTemplateId(id);
        return templateService.createOrUpdateTemplate(template);
    }

    @DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable int id) {
        templateService.deleteTemplate(id);
    }
}
