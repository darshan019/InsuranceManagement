package com.internship.InsuranceManagement.dto;

import com.internship.InsuranceManagement.entity.Category;
import com.internship.InsuranceManagement.entity.InsuranceType;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PolicyTemplateDTO {
    private int templateId;
    private AgentDTO agent;
    private InsuranceType insuranceType;
    private Category category;
    private String templateName;
    private double premiumAmount;
    private double coverageAmount;
}
