package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.PolicyTemplate;
import java.util.List;

public interface PolicyTemplateDAO {
    List<PolicyTemplate> findAll();
    PolicyTemplate findById(int id);
    PolicyTemplate save(PolicyTemplate template);
    void deleteById(int id);
    List<PolicyTemplate> findByAgentId(int agentId);
}
