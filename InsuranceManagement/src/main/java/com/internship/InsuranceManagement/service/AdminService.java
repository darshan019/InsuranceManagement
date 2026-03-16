package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.entity.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> findAll();
    Admin save(Admin admin);
    Admin findById(int id);
    void deleteById(int id);
}
