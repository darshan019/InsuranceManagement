package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface AgentDAO {
    List<Agent> findAll();
    Agent findById(int id);
    Agent save(Agent agent);
    void deleteById(int id);
    List<Customer> getCustomersByAgentId(int agentId);
}
