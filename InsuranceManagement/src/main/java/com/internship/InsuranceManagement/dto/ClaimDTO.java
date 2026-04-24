package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {
    private int claimId;
    private PolicyDTO policy;
    private LocalDateTime claimDate;
    private String description;
    private Double claimAmount;
    private String status;
    private AdminDTO approvedBy;
    private LocalDateTime approvedAt;
    private String remark;
}
