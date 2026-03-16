package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.InsuranceType;

import java.util.List;

public interface InsuranceTypeDAO {
    List<InsuranceType> findAll();
    InsuranceType findById(int id);
    InsuranceType save(InsuranceType insurancetype);
    void deleteById(int id);
}
