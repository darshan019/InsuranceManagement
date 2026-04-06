
package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.dto.ClaimDTO;
import com.internship.InsuranceManagement.dto.DTOMapper;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/claims/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<ClaimDTO> getNotApprovedClaims() {
        return claimService.findNotApproved().stream()
                .map(DTOMapper::toClaimDTO)
                .toList();
    }

    @GetMapping("/claims")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<ClaimDTO> getClaims() {
        return claimService.findAll().stream()
                .map(DTOMapper::toClaimDTO)
                .toList();
    }

    @GetMapping("/claims/{claimId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    public ClaimDTO getClaim(@PathVariable int claimId) {
        return DTOMapper.toClaimDTO(claimService.findById(claimId));
    }

    @PostMapping("/claims")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'AGENT')")
    public Claim postClaim(@RequestBody Claim claim) {
        claim.setClaimId(0);
        return claimService.save(claim);
    }

    @DeleteMapping("/claims/{claimId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteClaim(@PathVariable int claimId) {
        claimService.deleteById(claimId);
    }
}
