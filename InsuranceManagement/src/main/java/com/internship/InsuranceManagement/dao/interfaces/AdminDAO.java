package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;

import java.util.List;

public interface AdminDAO {
    List<Admin> findAll();
    Admin findById(int id);
    Admin save(Admin admin);
    void deleteById(int id);
    Claim approveClaimById(int claimId, int adminId) throws Exception;
}
