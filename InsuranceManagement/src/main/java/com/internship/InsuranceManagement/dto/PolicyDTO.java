package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDTO {
    private int policyId;
    private CustomerDTO customer;
    private PolicyTemplateDTO template;
    private String policyNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}
