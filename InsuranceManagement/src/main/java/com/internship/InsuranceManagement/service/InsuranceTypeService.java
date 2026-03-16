package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.entity.InsuranceType;

import java.util.List;

public interface InsuranceTypeService {
    List<InsuranceType> findAll();
    InsuranceType findById(int id);
    InsuranceType save(InsuranceType insurancetype);
    void deleteById(int id);
}
