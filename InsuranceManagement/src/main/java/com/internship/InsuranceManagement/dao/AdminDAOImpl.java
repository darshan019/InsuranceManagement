package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO{

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
}
