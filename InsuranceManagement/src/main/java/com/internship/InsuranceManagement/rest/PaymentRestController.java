package com.internship.InsuranceManagement.rest;


import com.internship.InsuranceManagement.entity.Payment;
import com.internship.InsuranceManagement.service.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentRestController {
    private final PaymentService PaymentService;

    @Autowired
    public PaymentRestController(PaymentService PaymentService) {
        this.PaymentService = PaymentService;
    }

    @GetMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<Payment> getPayments() {
        return PaymentService.findAll();
    }

    @PostMapping("/payment")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'AGENT')")
    public Payment postPayment(@RequestBody Payment payment) {
        payment.setPaymentId(0);
        return PaymentService.save(payment);
    }

    @DeleteMapping("/payment/{paymentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePayment(@PathVariable int paymentId) {
        PaymentService.deleteById(paymentId);
    }

}
