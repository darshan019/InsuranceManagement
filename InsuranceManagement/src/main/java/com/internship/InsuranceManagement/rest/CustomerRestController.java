package com.internship.InsuranceManagement.rest;

import java.util.List;

import com.internship.InsuranceManagement.dto.CustomerDTO;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.security.JwtUtil;
import com.internship.InsuranceManagement.service.interfaces.CustomerService;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    private final PolicyService policyService;
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    public CustomerRestController(PolicyService policyService,
                                  CustomerService customerService,
                                  JwtUtil jwtUtil) {
        this.policyService = policyService;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<CustomerDTO> getCustomers() {
        return customerService.findAll()
                .stream()
                .map(c -> new CustomerDTO(
                        c.getCustomerId(),
                        c.getUsername(),
                        c.getEmail(),
                        c.getAddress(),
                        c.getDateOfBirth()
                ))
                .toList();
    }


    @PostMapping("/")
    public Customer registerCustomer(@RequestBody Customer customer) {
        customer.setCustomerId(0); // ensure insert
        return customerService.save(customer);
    }




    @PostMapping("/agent/{customerId}/buyPolicy/{policyTemplateId}")
    @PreAuthorize("hasRole('AGENT')")
    public Policy agentBuyPolicy(@PathVariable int customerId,
                                 @PathVariable int policyTemplateId) {

        return policyService.buyPolicy(customerId, policyTemplateId);
    }


    @PostMapping("/buyPolicy/{policyTemplateId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Policy customerBuyPolicy(@PathVariable int policyTemplateId,
                                    @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        Customer customer = customerService.findByEmail(email);

        return policyService.buyPolicy(
                customer.getCustomerId(),
                policyTemplateId
        );
    }


    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable int customerId) {
        customerService.deleteById(customerId);
    }
}