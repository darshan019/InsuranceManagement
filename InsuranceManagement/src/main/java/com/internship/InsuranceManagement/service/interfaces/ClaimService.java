package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Claim;

import java.util.Collection;
import java.util.List;

public interface ClaimService {
    List<Claim> findAll();
    Claim save(Claim claim);
    Claim findById(int id);
    void deleteById(int id);
    List<Claim> findNotApproved();
    List<Claim> findByCustomerId(int customerId);
}
