package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Policy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public class PolicyDAOImpl implements PolicyDAO {
    private final EntityManager entityManager;

    @Autowired
    public PolicyDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Policy> findAll() {
        TypedQuery<Policy> query = entityManager.createQuery("from Policy", Policy.class);
        return query.getResultList();
    }

    @Override
    public Policy findById(int id) {
        return entityManager.find(Policy.class, id);
    }

    @Override
    public Policy save(Policy policy) {
        return entityManager.merge(policy);
    }

    @Override
    public void deleteById(int id) {
        Policy policy = entityManager.find(Policy.class, id);
        entityManager.remove(policy);
    }

    public List<Policy> findPoliciesByAgentId(@PathVariable int agentId) {
        String query = "SELECT p FROM Policy p WHERE p.agent.agentId = :agentId";
        return entityManager.createQuery(query, Policy.class).setParameter("agentId", agentId).getResultList();
    }
}
