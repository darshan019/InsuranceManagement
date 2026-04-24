package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.ClaimDAO;
import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.ClaimService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimDAO claimDAO;
    private final PolicyDAO policyDAO;

    @Autowired
    public ClaimServiceImpl(ClaimDAO claimDAO, PolicyDAO policyDAO) {
        this.claimDAO = claimDAO;
        this.policyDAO = policyDAO;
    }

    @Override
    public List<Claim> findNotApproved() {
        return claimDAO.findNotApproved();
    }

    @Override
    public List<Claim> findAll() {
        return claimDAO.findAll();
    }

    @Override
    @Transactional
    public Claim save(Claim claim) {
        // --- BUSINESS RULES ---
        if (claim.getPolicy() == null || claim.getPolicy().getPolicyId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Claim must reference a policy.");
        }

        int policyId = claim.getPolicy().getPolicyId();
        Policy policy = policyDAO.findById(policyId);
        if (policy == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Policy not found.");
        }

        // Rule 1: policy must be ACTIVE (premium paid and not overdue)
        String rawStatus = policy.getStatus() == null ? "" : policy.getStatus().toUpperCase();
        boolean isActiveStored = rawStatus.contains("ACTIVE");
        boolean isOverdue = policy.getNextPremiumDate() != null
                && policy.getNextPremiumDate().isBefore(LocalDateTime.now());

        if (!isActiveStored) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You can only file a claim on an ACTIVE policy. "
                            + "Current status: " + policy.getStatus());
        }
        if (isOverdue) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Premium is overdue. Please pay the premium before filing a claim.");
        }

        // Rule 2: one claim per policy — only block when a new one is being created
        if (claim.getClaimId() == 0 && claimDAO.existsByPolicyId(policyId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A claim has already been filed for this policy. "
                            + "Only one claim per policy is allowed.");
        }

        // Default status
        if (claim.getStatus() == null || claim.getStatus().isEmpty()) {
            claim.setStatus("PENDING");
        }

        return claimDAO.save(claim);
    }

    @Override
    public Claim findById(int id) {
        return claimDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        claimDAO.deleteById(id);
    }
}
