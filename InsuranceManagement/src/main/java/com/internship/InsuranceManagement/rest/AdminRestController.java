package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('ADMIN')")
    public List<Admin> getAdmins() {
        return adminService.findAll();
    }

    @PatchMapping("/admins/{adminId}/{claimId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public Claim approveClaim(@PathVariable int claimId,@PathVariable int adminId) throws Exception {
        return adminService.approveClaimById(claimId,adminId);
    }


}
