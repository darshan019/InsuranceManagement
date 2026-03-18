package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.AdminDAO;
import com.internship.InsuranceManagement.dao.interfaces.ClaimDAO;
import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {

    private final EntityManager entityManager;
    @Autowired
    public AdminDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Admin> findAll() {
        TypedQuery<Admin> query = entityManager.createQuery("from Admin", Admin.class);
        return query.getResultList();
    }

    @Override
    public Admin findById(int id) {
        return entityManager.find(Admin.class, id);
    }

    @Override
    public Admin save(Admin admin) {
        return entityManager.merge(admin);
    }

    @Override
    public void deleteById(int id) {
        Admin admin = entityManager.find(Admin.class, id);
        entityManager.remove(admin);

    }

    @Override
    @Transactional
    public Claim approveClaimById(int claimId, int adminId) throws Exception {
        Admin admin = entityManager.find(Admin.class, adminId);

        if (admin == null) {
            throw new Exception("Admin does not exist");
        }

        String q = "UPDATE Claim c SET c.status = :status, c.approvedBy = :admin, c.approvedAt = CURRENT_TIMESTAMP WHERE c.claimId = :claimId";
        entityManager.createQuery(q)
                .setParameter("status", "Approved")
                .setParameter("admin", admin)   // pass the entity, not the ID
                .setParameter("claimId", claimId)
                .executeUpdate();

        return entityManager.find(Claim.class, claimId);
    }
}
