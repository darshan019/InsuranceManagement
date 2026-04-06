package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.LoginAuditDAO;
import com.internship.InsuranceManagement.entity.LoginAudit;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class LoginAuditDAOImpl implements LoginAuditDAO {

    private final EntityManager entityManager;

    public LoginAuditDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(LoginAudit audit) {
        entityManager.persist(audit);
    }
}

