package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.InsuranceTypeDAO;
import com.internship.InsuranceManagement.entity.InsuranceType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InsuranceTypeDAOImpl implements InsuranceTypeDAO {
    private final EntityManager entityManager;
    @Autowired
    public InsuranceTypeDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<InsuranceType> findAll() {
        TypedQuery<InsuranceType> query = entityManager.createQuery("from InsuranceType", InsuranceType.class);
        return query.getResultList();
    }

    @Override
    public InsuranceType findById(int id) {
        return entityManager.find(InsuranceType.class,id);
    }

    @Override
    public InsuranceType save(InsuranceType insurancetype) {
        return entityManager.merge(insurancetype);
    }

    @Override
    public void deleteById(int id) {
        InsuranceType insurancetype= entityManager.find(InsuranceType.class,id);
        entityManager.remove(insurancetype);
    }
}
