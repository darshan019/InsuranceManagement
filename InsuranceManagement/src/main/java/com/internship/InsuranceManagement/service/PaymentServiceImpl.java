package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.dao.PaymentDAO;
import com.internship.InsuranceManagement.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private PaymentDAO paymentDAO;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }


    @Override
    public List<Payment> findAll() {
        return paymentDAO.findAll();
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        return paymentDAO.save(payment);
    }

    @Override
    public Payment findById(int id) {
        return paymentDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        paymentDAO.deleteById(id);
    }
}
