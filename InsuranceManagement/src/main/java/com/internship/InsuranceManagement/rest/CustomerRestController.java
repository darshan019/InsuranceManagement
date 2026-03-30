package com.internship.InsuranceManagement.rest;

import java.util.List;

import com.internship.InsuranceManagement.dto.CustomerDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.CustomerService;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;

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
    public List<CustomerDTO> getCustomers() {
        List<Customer> customers = customerService.findAll();
        return customers.stream()
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
