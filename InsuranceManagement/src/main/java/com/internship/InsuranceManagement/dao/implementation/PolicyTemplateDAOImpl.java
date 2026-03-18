package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PolicyTemplateDAO;
import com.internship.InsuranceManagement.entity.PolicyTemplate;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PolicyTemplateDAOImpl implements PolicyTemplateDAO {

    private final EntityManager entityManager;

    @Autowired
    public PolicyTemplateDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<PolicyTemplate> findAll() {
        TypedQuery<PolicyTemplate> query = entityManager.createQuery("FROM PolicyTemplate", PolicyTemplate.class);
        return query.getResultList();
    }

    @Override
    public PolicyTemplate findById(int id) {
        return entityManager.find(PolicyTemplate.class, id);
    }

    @Override
    public PolicyTemplate save(PolicyTemplate template) {
        return entityManager.merge(template);
    }

    @Override
    public void deleteById(int id) {
        PolicyTemplate template = entityManager.find(PolicyTemplate.class, id);
        if (template != null) {
            entityManager.remove(template);
        }
    }

    @Override
    public List<PolicyTemplate> findByAgentId(int agentId) {
        String query = "SELECT t FROM PolicyTemplate t WHERE t.agent.agentId = :agentId";
        return entityManager.createQuery(query, PolicyTemplate.class)
                .setParameter("agentId", agentId)
                .getResultList();
    }
}
