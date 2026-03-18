package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PolicyTemplateDAO;
import com.internship.InsuranceManagement.entity.PolicyTemplate;
import com.internship.InsuranceManagement.service.interfaces.PolicyTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyTemplateServiceImpl implements PolicyTemplateService {

    private final PolicyTemplateDAO templateDAO;

    @Autowired
    public PolicyTemplateServiceImpl(PolicyTemplateDAO templateDAO) {
        this.templateDAO = templateDAO;
    }

    @Override
    public List<PolicyTemplate> getAllTemplates() {
        return templateDAO.findAll();
    }

    @Override
    public PolicyTemplate getTemplateById(int id) {
        return templateDAO.findById(id);
    }

    @Override
    public PolicyTemplate createOrUpdateTemplate(PolicyTemplate template) {
        return templateDAO.save(template);
    }

    @Override
    public void deleteTemplate(int id) {
        templateDAO.deleteById(id);
    }

    @Override
    public List<PolicyTemplate> getTemplatesByAgentId(int agentId) {
        return templateDAO.findByAgentId(agentId);
    }
}
