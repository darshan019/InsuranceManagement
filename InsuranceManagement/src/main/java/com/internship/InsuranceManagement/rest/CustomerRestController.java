package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.CustomerService;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {
    private final PolicyService PolicyService;
    private final CustomerService customerService;

    public CustomerRestController(PolicyService policyService, CustomerService customerService) {
        PolicyService = policyService;
        this.customerService = customerService;
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public Customer postCustomer(@RequestBody Customer customer) {
        customer.setCustomerId(0);
        return customerService.save(customer);
    }

    @PostMapping("/{customerId}/buyPolicy/{policyTemplateId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'AGENT')")
    public Policy buyPolicy(@PathVariable int customerId, @PathVariable int policyTemplateId) {
        return PolicyService.buyPolicy(customerId, policyTemplateId);
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(@PathVariable int customerId) {
        customerService.deleteById(customerId);
    }

}
