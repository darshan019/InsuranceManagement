package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/claims/{claimId}")
    public Claim getClaim(@PathVariable int claimId) {
        return claimService.findById(claimId);
    }

    @PostMapping("/claims")
    public Claim postClaim(@RequestBody Claim claim) {
        claim.setClaimId(0);
        return claimService.save(claim);
    }

    @DeleteMapping("/claims/{claimId}")
    public void deleteClaim(@PathVariable int claimId) {
        claimService.deleteById(claimId);
    }
}
