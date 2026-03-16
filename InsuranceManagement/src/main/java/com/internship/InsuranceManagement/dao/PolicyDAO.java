package com.internship.InsuranceManagement.dao;
import com.internship.InsuranceManagement.entity.Policy;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface PolicyDAO {
    List<Policy> findAll();
    Policy findById(int id);
    Policy save(Policy policy);
    void deleteById(int id);
    List<Policy> findPoliciesByAgentId(int agentId);

}
