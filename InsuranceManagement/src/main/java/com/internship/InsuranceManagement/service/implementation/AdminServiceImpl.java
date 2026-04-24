package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.AdminDAO;
import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO;

    @Autowired
    public AdminServiceImpl(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    @Override
    public List<Admin> findAll() {
        return adminDAO.findAll();
    }

    @Override
    public Admin save(Admin admin) {
        return adminDAO.save(admin);
    }

    @Override
    public Admin findById(int id) {
        return adminDAO.findById(id);
    }

    @Override
    public void deleteById(int id) {
        adminDAO.deleteById(id);
    }

    @Override
    public Claim approveClaimById(int claimId, int adminId) throws Exception {
        return adminDAO.approveClaimById(claimId, adminId);
    }

    @Override
    public Claim rejectClaimById(int claimId, int adminId, String remark) throws Exception {
        return adminDAO.rejectClaimById(claimId, adminId, remark);
    }
}
