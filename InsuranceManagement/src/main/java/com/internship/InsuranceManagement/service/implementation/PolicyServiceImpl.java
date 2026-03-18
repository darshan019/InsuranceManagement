package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {
    private final PolicyDAO policyDAO;

    @Autowired
    public PolicyServiceImpl(PolicyDAO policyDAO) {
        this.policyDAO = policyDAO;
    }

    @Override
    public List<Policy> findAll() {
        return policyDAO.findAll();
    }

    @Override
    @Transactional
    public Policy save(Policy policy) {
        return policyDAO.save(policy);
    }

    @Override
    public Policy findById(int id) {
        return policyDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        policyDAO.deleteById(id);
    }

    public List<Policy> findPoliciesOfAgent(int agentId) {
        return policyDAO.findPoliciesByAgentId(agentId);
    }

    @Override
    public List<Policy> findPoliciesByCustomerId(int customerId) {
        return policyDAO.findPoliciesByCustomerId(customerId);
    }
}
