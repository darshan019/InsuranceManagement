package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Agent save(Agent agent);
    Agent findById(int id);
    void deleteById(int id);
    List<Customer> getCustomers(int agentId);
}
