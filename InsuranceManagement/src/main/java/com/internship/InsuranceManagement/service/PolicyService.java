package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.entity.Policy;

import java.util.List;

public interface PolicyService {
    List<Policy> findAll();
    Policy save(Policy policy);
    Policy findById(int id);
    void deleteById(int id);
}
