package com.internship.InsuranceManagement.auth.service;

import com.internship.InsuranceManagement.auth.config.JwtUtil;
import com.internship.InsuranceManagement.auth.dto.AuthResponse;
import com.internship.InsuranceManagement.auth.dto.LoginRequest;
import com.internship.InsuranceManagement.auth.dto.RegisterRequest;
import com.internship.InsuranceManagement.auth.repository.AdminAuthRepository;
import com.internship.InsuranceManagement.auth.repository.AgentAuthRepository;
import com.internship.InsuranceManagement.auth.repository.CustomerAuthRepository;
import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Customer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {

    private final CustomerAuthRepository customerRepo;
    private final AdminAuthRepository adminRepo;
    private final JwtUtil jwtUtil;
    private final AgentAuthRepository agentRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(CustomerAuthRepository customerRepo,
                       AdminAuthRepository adminRepo,
                       AgentAuthRepository agentRepo,
                       JwtUtil jwtUtil) {
        this.customerRepo = customerRepo;
        this.adminRepo = adminRepo;
        this.agentRepo = agentRepo;
        this.jwtUtil = jwtUtil;
    }

    // -----------------------------
    // CUSTOMER REGISTRATION
    // -----------------------------
    public AuthResponse register(RegisterRequest req) {

        if (customerRepo.existsByEmail(req.getEmail())) {
            return new AuthResponse("Email already exists!", null);
        }

        Customer c = new Customer();
        c.setUsername(req.getUsername());
        c.setEmail(req.getEmail());
        c.setPassword(encoder.encode(req.getPassword()));  // HASH password
        c.setAddress(req.getAddress());
        c.setDateOfBirth(LocalDate.parse(req.getDateOfBirth()));
        c.setPhoneNumber(req.getPhoneNumber());
        c.setRole("USER");

        customerRepo.save(c);

        return new AuthResponse("Registered successfully!", null);
    }

    // -----------------------------
    // SMART LOGIN (Customer + Admin)
    // -----------------------------
    public AuthResponse login(LoginRequest req) {

        // Step 1: Check CUSTOMER table
        var customer = customerRepo.findByEmail(req.getEmail());
        if (customer.isPresent()) {

            Customer c = customer.get();

            if (!encoder.matches(req.getPassword(), c.getPassword())) {
                throw new RuntimeException("Invalid password!");
            }

            String token = jwtUtil.generateToken(c.getEmail(), c.getRole());
            return new AuthResponse("Customer login successful!", token);
        }

        // Step 2: Check ADMIN table
        var admin = adminRepo.findByEmail(req.getEmail());
        if (admin.isPresent()) {

            Admin a = admin.get();

            // NOTE: Admin passwords in your DB are plain text
            if (!a.getPassword().equals(req.getPassword())) {
                throw new RuntimeException("Invalid admin password!");
            }

            String token = jwtUtil.generateToken(a.getEmail(), "ADMIN");
            return new AuthResponse("Admin login successful!", token);
        }



        // Step 3: Nobody found
        throw new RuntimeException("User not found!");
    }
}