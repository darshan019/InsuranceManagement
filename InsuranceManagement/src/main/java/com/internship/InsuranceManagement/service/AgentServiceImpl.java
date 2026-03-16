package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.dao.AgentDAO;
import com.internship.InsuranceManagement.entity.Agent;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentDAO agentDAO;

    @Autowired
    public AgentServiceImpl(AgentDAO agentDAO) {
        this.agentDAO = agentDAO;
    }

    @Override
    public List<Agent> findAll() {
        return agentDAO.findAll();
    }

    @Override
    @Transactional
    public Agent save(Agent agent) {
        return agentDAO.save(agent);
    }

    @Override
    public Agent findById(int id) {
        return agentDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        agentDAO.deleteById(id);
    }
}
