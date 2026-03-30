package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;

public class PaymentDTO {
    private int paymentId;
    private CustomerDTO customer;
    private PolicyDTO policy;
    private LocalDateTime paymentDate;
    private Double amount;
    private String paymentMethod;
    private String status;

    public PaymentDTO() {}

    public PaymentDTO(int paymentId, CustomerDTO customer, PolicyDTO policy,
                      LocalDateTime paymentDate, Double amount, String paymentMethod, String status) {
        this.paymentId = paymentId;
        this.customer = customer;
        this.policy = policy;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public PolicyDTO getPolicy() {
        return policy;
    }

    public void setPolicy(PolicyDTO policy) {
        this.policy = policy;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
