package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;

import java.util.List;

public interface AdminService {
    List<Admin> findAll();
    Admin save(Admin admin);
    Admin findById(int id);
    void deleteById(int id);
    Claim approveClaimById(int claimId, int adminId) throws Exception;
    Claim rejectClaimById(int claimId, int adminId, String remark) throws Exception;
}
