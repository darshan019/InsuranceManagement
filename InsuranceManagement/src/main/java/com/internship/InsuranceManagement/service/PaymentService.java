package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> findAll();
    Payment save(Payment payment);
    Payment findById(int id);
    void deleteById(int id);
}
