package com.internship.InsuranceManagement.rest;


import com.internship.InsuranceManagement.entity.Payment;
import com.internship.InsuranceManagement.service.PaymnetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentRestController {
    private PaymnetService paymnetService;

    @Autowired
    public PaymentRestController(PaymnetService paymnetService) {
        this.paymnetService = paymnetService;
    }

    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return paymnetService.findAll();
    }
}
