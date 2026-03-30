package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.dto.DTOMapper;
import com.internship.InsuranceManagement.dto.PolicyTemplateDTO;
import com.internship.InsuranceManagement.entity.PolicyTemplate;
import com.internship.InsuranceManagement.service.interfaces.PolicyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    public List<PolicyTemplateDTO> getAllTemplates() {
        return templateService.getAllTemplates().stream()
                .map(DTOMapper::toPolicyTemplateDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    public PolicyTemplateDTO getTemplateById(@PathVariable int id) {
        return DTOMapper.toPolicyTemplateDTO(templateService.getTemplateById(id));
    }

    @GetMapping("/agent/{agentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<PolicyTemplateDTO> getTemplatesByAgent(@PathVariable int agentId) {
        return templateService.getTemplatesByAgentId(agentId).stream()
                .map(DTOMapper::toPolicyTemplateDTO)
                .toList();
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public PolicyTemplate createTemplate(@RequestBody PolicyTemplate template) {
        return templateService.createOrUpdateTemplate(template);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public PolicyTemplate updateTemplate(@PathVariable int id, @RequestBody PolicyTemplate template) {
        template.setTemplateId(id);
        return templateService.createOrUpdateTemplate(template);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTemplate(@PathVariable int id) {
        templateService.deleteTemplate(id);
    }
}
