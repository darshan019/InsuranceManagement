package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.ClaimDAO;
import com.internship.InsuranceManagement.entity.Claim;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClaimDAOImpl implements ClaimDAO {
    private final EntityManager entityManager;

    @Autowired
    public ClaimDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Claim> findNotApproved() {
        TypedQuery<Claim> query = entityManager.createQuery(
                "from Claim c where c.status != 'approved'", Claim.class);
        return query.getResultList();
    }

    public List<Claim> findAll() {
        TypedQuery<Claim> query = entityManager.createQuery("from Claim", Claim.class);
        return query.getResultList();
    }

    @Override
    public Claim findById(int id) {
        return entityManager.find(Claim.class, id);
    }

    @Override
    public Claim save(Claim claim) {
        return entityManager.merge(claim);
    }

    @Override
    public void deleteById(int id) {
        Claim c = entityManager.find(Claim.class, id);
        entityManager.remove(c);
    }

    @Override
    public boolean existsByPolicyId(int policyId) {
        Long count = entityManager.createQuery(
                        "SELECT COUNT(c) FROM Claim c WHERE c.policy.policyId = :pid",
                        Long.class)
                .setParameter("pid", policyId)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public List<Claim> findByCustomerId(int customerId) {
        return entityManager.createQuery(
                        "FROM Claim c WHERE c.customer.customerId = :cid " +
                                "OR (c.customer IS NULL AND c.policy.customer.customerId = :cid)",
                        Claim.class)
                .setParameter("cid", customerId)
                .getResultList();
    }
}
