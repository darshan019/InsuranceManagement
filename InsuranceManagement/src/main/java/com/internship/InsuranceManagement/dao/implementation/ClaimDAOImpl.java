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
        TypedQuery<Claim> query = entityManager.createQuery("from Claim c where c.status != 'approved'", Claim.class);
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
        Claim Claim = entityManager.find(Claim.class, id);
        entityManager.remove(Claim);
    }
}
