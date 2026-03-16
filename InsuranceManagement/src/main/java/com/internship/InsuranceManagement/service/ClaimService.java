package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.entity.Customer;

import java.util.List;

public interface ClaimService {
    List<Claim> findAll();
    Claim save(Claim claim);
    Claim findById(int id);
    void deleteById(int id);
}
