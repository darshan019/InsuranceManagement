package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {
    private final AdminService adminService;

    @Autowired
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admins")
    public List<Admin> getAdmins() {
        return adminService.findAll();
    }
}
