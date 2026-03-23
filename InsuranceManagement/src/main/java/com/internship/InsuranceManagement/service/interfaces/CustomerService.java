package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();
    Customer save(Customer customer);
    Customer findById(int id);
    Customer findByEmail(String email);
    void deleteById(int id);
}
