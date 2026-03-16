package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.InsuranceType;
import com.internship.InsuranceManagement.service.InsuranceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InsuranceTypeController {
    private final InsuranceTypeService insuranceTypeService;
    @Autowired
    public InsuranceTypeController(InsuranceTypeService insuranceTypeService) {
        this.insuranceTypeService = insuranceTypeService;
    }

    @GetMapping("/insurance_types")
    public List<InsuranceType> getInsuranceTypes() {
        return insuranceTypeService.findAll();
    }
}
