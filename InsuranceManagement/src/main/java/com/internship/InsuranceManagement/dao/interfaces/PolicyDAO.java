package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Policy;

import java.util.List;

public interface PolicyDAO {
    List<Policy> findAll();

    Policy findById(int id);

    Policy save(Policy policy);

    void deleteById(int id);

    List<Policy> findPoliciesByAgentId(int agentId);
    List<Policy> findPoliciesByCustomerId(int customerId);

}
