package com.internship.InsuranceManagement.dto;

import com.internship.InsuranceManagement.entity.Category;
import com.internship.InsuranceManagement.entity.InsuranceType;

public class PolicyTemplateDTO {
    private int templateId;
    private AgentDTO agent;
    private InsuranceType insuranceType;
    private Category category;
    private String templateName;
    private double premiumAmount;
    private double coverageAmount;

    public PolicyTemplateDTO() {}

    public PolicyTemplateDTO(int templateId, AgentDTO agent, InsuranceType insuranceType,
                             Category category, String templateName, double premiumAmount, double coverageAmount) {
        this.templateId = templateId;
        this.agent = agent;
        this.insuranceType = insuranceType;
        this.category = category;
        this.templateName = templateName;
        this.premiumAmount = premiumAmount;
        this.coverageAmount = coverageAmount;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public AgentDTO getAgent() {
        return agent;
    }

    public void setAgent(AgentDTO agent) {
        this.agent = agent;
    }

    public InsuranceType getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(InsuranceType insuranceType) {
        this.insuranceType = insuranceType;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(double premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public double getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(double coverageAmount) {
        this.coverageAmount = coverageAmount;
    }
}
