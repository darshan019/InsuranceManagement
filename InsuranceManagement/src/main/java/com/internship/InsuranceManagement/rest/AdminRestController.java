package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public Claim approveClaim(@PathVariable int claimId,
                              @PathVariable int adminId) throws Exception {
        return adminService.approveClaimById(claimId, adminId);
    }

    // Reject now takes an optional JSON body { "remark": "reason text..." }
    @PatchMapping("/admins/{adminId}/{claimId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectClaim(@PathVariable int claimId,
                                         @PathVariable int adminId,
                                         @RequestBody(required = false) Map<String, String> body) throws Exception {
        String remark = body == null ? null : body.get("remark");
        if (remark == null || remark.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A remark is required when rejecting a claim.");
        }
        Claim c = adminService.rejectClaimById(claimId, adminId, remark.trim());
        return ResponseEntity.ok(c);
    }
}
