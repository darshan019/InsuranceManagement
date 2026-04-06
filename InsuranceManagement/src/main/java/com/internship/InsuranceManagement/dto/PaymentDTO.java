package com.internship.InsuranceManagement.dto;

import java.time.LocalDateTime;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private int paymentId;
    private CustomerDTO customer;
    private PolicyDTO policy;
    private LocalDateTime paymentDate;
    private Double amount;
    private String paymentMethod;
    private String status;
}
