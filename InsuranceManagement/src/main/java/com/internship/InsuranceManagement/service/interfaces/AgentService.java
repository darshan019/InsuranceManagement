package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Agent;

import java.util.List;

public interface AgentService {
    List<Agent> findAll();
    Agent save(Agent agent);
    Agent findById(int id);
    void deleteById(int id);
}
