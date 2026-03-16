package com.internship.InsuranceManagement.service;

import com.internship.InsuranceManagement.dao.InsuranceTypeDAO;
import com.internship.InsuranceManagement.entity.InsuranceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceTypeServiceImpl implements InsuranceTypeService{
    private final InsuranceTypeDAO insuranceTypeDAO;
    @Autowired
    public InsuranceTypeServiceImpl(InsuranceTypeDAO insuranceTypeDAO) {
        this.insuranceTypeDAO = insuranceTypeDAO;
    }

    @Override
    public List<InsuranceType> findAll() {
        return insuranceTypeDAO.findAll();
    }

    @Override
    public InsuranceType findById(int id) {
        return insuranceTypeDAO.findById(id);
    }

    @Override
    public InsuranceType save(InsuranceType insurancetype) {
        return insuranceTypeDAO.save(insurancetype);
    }

    @Override
    public void deleteById(int id) {
        insuranceTypeDAO.deleteById(id);

    }
}
