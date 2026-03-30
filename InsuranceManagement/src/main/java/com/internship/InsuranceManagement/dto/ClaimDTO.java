package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;

public class ClaimDTO {
    private int claimId;
    private PolicyDTO policy;
    private LocalDateTime claimDate;
    private String description;
    private Double claimAmount;
    private String status;
    private AdminDTO approvedBy;
    private LocalDateTime approvedAt;

    public ClaimDTO() {}

    public ClaimDTO(int claimId, PolicyDTO policy, LocalDateTime claimDate, String description,
                    Double claimAmount, String status, AdminDTO approvedBy, LocalDateTime approvedAt) {
        this.claimId = claimId;
        this.policy = policy;
        this.claimDate = claimDate;
        this.description = description;
        this.claimAmount = claimAmount;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public PolicyDTO getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyDTO policy) {
        this.policy = policy;
    }

    public LocalDateTime getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDateTime claimDate) {
        this.claimDate = claimDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AdminDTO getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(AdminDTO approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }
}
