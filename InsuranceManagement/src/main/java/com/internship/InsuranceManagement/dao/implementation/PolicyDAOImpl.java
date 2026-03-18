package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Policy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<Policy> findPoliciesByAgentId(int agentId) {
        String query = "SELECT p FROM Policy p WHERE p.agent.agentId = :agentId";
        return entityManager.createQuery(query, Policy.class).setParameter("agentId",
                agentId).getResultList();
    }

    @Override
    public List<Policy> findPoliciesByCustomerId(int customerId) {
        String query = "SELECT p FROM Policy p WHERE p.customer.customerId = :customerId";
        return entityManager.createQuery(query, Policy.class).setParameter("customerId",
                customerId).getResultList();
    }

    @Override
    public Policy checkPolicyPayment(int policyId) {
        String query = "SELECT p.paymentDate FROM Payment p WHERE p.policy.policyId = :policyId ORDER BY p.paymentDate DESC";
        List<LocalDateTime> results = entityManager.createQuery(query, LocalDateTime.class)
                .setParameter("policyId", policyId)
                .setMaxResults(1)
                .getResultList();

        Policy policy = entityManager.find(Policy.class, policyId);

        if (!results.isEmpty()) {
            LocalDate lastPaymentDate = LocalDate.from(results.getFirst());

            // Check if 30 days have passed
            if (lastPaymentDate.plusDays(30).isBefore(LocalDate.now())) {
                policy.setStatus("Payment Pending");
                entityManager.merge(policy); // update status in DB
            } else {
                policy.setStatus("Payment Paid");
                entityManager.merge(policy);
            }
        } else {
            // No payment found at all
            policy.setStatus("Payment Pending");
            entityManager.merge(policy);
        }

        return policy;
    }

}
