package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PaymentDAO;
import com.internship.InsuranceManagement.entity.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PaymentDAOImpl implements PaymentDAO {
    private final EntityManager entityManager;

    @Autowired
    public PaymentDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Payment> findAll() {
        TypedQuery<Payment> query = entityManager.createQuery("from Payment" ,Payment.class);
        return query.getResultList();
    }

    @Override
    public Payment findById(int id) {
        return entityManager.find(Payment.class, id);
    }

    @Override
    public Payment save(Payment payment) {
        return entityManager.merge(payment);
    }

    @Override
    public void deleteById(int id) {
        Payment payment = entityManager.find(Payment.class, id);
        entityManager.remove(payment);
    }



}





