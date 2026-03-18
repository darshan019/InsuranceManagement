package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.AgentDAO;
import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AgentDAOImpl implements AgentDAO {

    private final EntityManager entityManager;
    @Autowired
    public AgentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<Agent> findAll() {
        TypedQuery<Agent> query = entityManager.createQuery("from Agent", Agent.class);
        return query.getResultList();
    }

    @Override
    public Agent findById(int id) {
        return entityManager.find(Agent.class, id);
    }

    @Override
    public Agent save(Agent agent) {
        return entityManager.merge(agent);
    }

    @Override
    public void deleteById(int id) {
        Agent agent = entityManager.find(Agent.class, id);
        entityManager.remove(agent);

    }

    @Override
    public List<Customer> getCustomersByAgentId(int agentId) {
        String jpql = "SELECT p.customer FROM Policy p WHERE p.template.agent.agentId = :agentId";
        return entityManager.createQuery(jpql, Customer.class)
                .setParameter("agentId", agentId)
                .getResultList();
    }
}
