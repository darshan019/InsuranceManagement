package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.Admin;

import java.util.List;

public interface AdminDAO {
    List<Admin> findAll();
    Admin findById(int id);
    Admin save(Admin admin);
    void deleteById(int id);
}
