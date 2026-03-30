package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;

public class PolicyDTO {
    private int policyId;
    private CustomerDTO customer;
    private PolicyTemplateDTO template;
    private String policyNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;

    public PolicyDTO() {}

    public PolicyDTO(int policyId, CustomerDTO customer, PolicyTemplateDTO template,
                     String policyNumber, LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.policyId = policyId;
        this.customer = customer;
        this.template = template;
        this.policyNumber = policyNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getPolicyId() {
        return policyId;
    }

    public void setPolicyId(int policyId) {
        this.policyId = policyId;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public PolicyTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(PolicyTemplateDTO template) {
        this.template = template;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
