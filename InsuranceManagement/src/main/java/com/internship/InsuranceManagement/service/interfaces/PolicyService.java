package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Policy;

import java.util.List;

public interface PolicyService {
    List<Policy> findAll();
    Policy save(Policy policy);
    Policy findById(int id);
    void deleteById(int id);
    List<Policy> findPoliciesOfAgent(int agentId);
    List<Policy> findPoliciesByCustomerId(int customerId);
    Policy checkPolicyPayment(int policyId);
    Policy buyPolicy(int customerId, int templateId);
}
