package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.dao.AdminDAO;
import com.internship.InsuranceManagement.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{
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
}
