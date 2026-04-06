package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Claim;

import java.util.List;

public interface ClaimDAO {
    List<Claim> findAll();
    Claim findById(int id);
    Claim save(Claim claim);
    void deleteById(int id);

    List<Claim> findNotApproved();
}
