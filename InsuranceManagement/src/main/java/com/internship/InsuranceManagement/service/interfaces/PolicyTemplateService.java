package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.PolicyTemplate;
import java.util.List;

public interface PolicyTemplateService {
    List<PolicyTemplate> getAllTemplates();
    PolicyTemplate getTemplateById(int id);
    PolicyTemplate createOrUpdateTemplate(PolicyTemplate template);
    void deleteTemplate(int id);
    List<PolicyTemplate> getTemplatesByAgentId(int agentId);
}
