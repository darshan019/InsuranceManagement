package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.Agent;

import java.util.List;

public interface AgentDAO {
    List<Agent> findAll();
    Agent findById(int id);
    Agent save(Agent agent);
    void deleteById(int id);
}
