package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface CustomerDAO {
    List<Customer> findAll();
    Customer findById(int id);
    Customer save(Customer customer);
    void deleteById(int id);
}
