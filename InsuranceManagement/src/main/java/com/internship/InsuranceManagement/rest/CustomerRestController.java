package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.service.interfaces.CustomerService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerRestController {
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

    @PostMapping("/customers")
    public Customer postCustomer(@RequestBody Customer customer) {
        customer.setCustomerId(0);
        return customerService.save(customer);
    }

    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomer(@PathVariable int customerId) {
        customerService.deleteById(customerId);
    }
    @GetMapping("/customers/me")
    public Customer getMyDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer c = customerService.findByEmail(email);
        if (c == null) {
            throw new RuntimeException("User not found in customer table.");
        }
        return c;
    }


}
