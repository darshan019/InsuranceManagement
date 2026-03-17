package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Payment;

import java.util.List;

public interface PaymentDAO {
    List<Payment> findAll();
    Payment findById(int id);
    Payment save(Payment payment);
    void deleteById(int id);

}
