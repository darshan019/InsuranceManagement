package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ClaimRestController {
    private final ClaimService claimService;

    @Autowired
    public ClaimRestController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/claims")
    public List<Claim> getClaims() {
        return claimService.findAll();
    }
}
