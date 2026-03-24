package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> findAll();
    Customer findById(int id);
    Customer save(Customer customer);
    Customer findByEmail(String email);
    void deleteById(int id);
}
