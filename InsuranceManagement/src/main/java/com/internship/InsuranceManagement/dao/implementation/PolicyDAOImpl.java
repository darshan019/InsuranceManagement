package com.internship.InsuranceManagement.dao.implementation;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Payment;
import com.internship.InsuranceManagement.entity.Policy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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
        String query = "SELECT p FROM Policy p WHERE p.template.agent.agentId = :agentId";
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
        String query = "SELECT p FROM Payment p WHERE p.policy.policyId = :policyId ORDER BY p.paymentDate DESC";

        List<Payment> payments = entityManager.createQuery(query, Payment.class)
                .setParameter("policyId", policyId)
                .setMaxResults(1)
                .getResultList();

        Policy policy = entityManager.find(Policy.class, policyId);

        if (!payments.isEmpty()) {
            Payment Payment = payments.get(0);
            LocalDate lastPaymentDate = Payment.getPaymentDate().toLocalDate();

            if (!lastPaymentDate.plusDays(365).isAfter(LocalDate.now())) {
                policy.setStatus("Due");
                Payment.setStatus("Payment Pending");
            } else {
                policy.setStatus("Active");
                Payment.setStatus("Payment Completed");
            }

            entityManager.merge(Payment);
        } else {
            policy.setStatus("Payment Pending");
        }
        entityManager.merge(policy);
        return policy;
    }

    @Transactional
    @Override
    public void cancelPolicyByPolicyNumber(String policyNumber, int customerId) {

        String jpql = """
        UPDATE Policy p
        SET p.status = 'CANCELLED'
        WHERE p.policyNumber = :policyNumber
        AND p.customer.customerId = :customerId
    """;

        int updated = entityManager.createQuery(jpql)
                .setParameter("policyNumber", policyNumber)
                .setParameter("customerId", customerId)
                .executeUpdate();

        if (updated == 0) {
            throw new RuntimeException("Policy not found or not owned by customer");
        }
    }
}
