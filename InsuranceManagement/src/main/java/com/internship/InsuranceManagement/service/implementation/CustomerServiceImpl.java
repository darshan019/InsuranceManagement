package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.CustomerDAO;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.service.interfaces.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerDAO customerDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.customerDAO = customerDAO;
    }

    @Override
    public List<Customer> findAll() {
        return customerDAO.findAll();
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {

        customer.setPassword(
                passwordEncoder.encode(customer.getPassword())
        );

        return customerDAO.save(customer);
    }


    @Override
    public Customer findById(int id) {
        return customerDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        customerDAO.deleteById(id);
    }
}
