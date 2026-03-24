package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.dto.LoginRequest;
import com.internship.InsuranceManagement.dto.LoginResponse;
import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.security.JwtUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EntityManager entityManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(EntityManager entityManager, JwtUtil jwtUtil) {
        this.entityManager = entityManager;
        this.jwtUtil = jwtUtil;
    }

    // ===================== ADMIN LOGIN =====================
    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequest loginRequest) {
        TypedQuery<Admin> query = entityManager.createQuery(
                "SELECT a FROM Admin a WHERE a.email = :email", Admin.class);
        query.setParameter("email", loginRequest.getEmail());

        List<Admin> admins = query.getResultList();
        if (admins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        Admin admin = admins.get(0);
        if (!admin.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
        return ResponseEntity.ok(new LoginResponse(token, "ADMIN", admin.getEmail()));
    }

    // ===================== AGENT LOGIN =====================
    @PostMapping("/agent/login")
    public ResponseEntity<?> agentLogin(@RequestBody LoginRequest loginRequest) {
        TypedQuery<Agent> query = entityManager.createQuery(
                "SELECT a FROM Agent a WHERE a.email = :email", Agent.class);
        query.setParameter("email", loginRequest.getEmail());

        List<Agent> agents = query.getResultList();
        if (agents.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        Agent agent = agents.get(0);
        if (!agent.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = jwtUtil.generateToken(agent.getEmail(), "AGENT");
        return ResponseEntity.ok(new LoginResponse(token, "AGENT", agent.getEmail()));
    }

    // ===================== CUSTOMER LOGIN =====================
    @PostMapping("/customer/login")
    public ResponseEntity<?> customerLogin(@RequestBody LoginRequest loginRequest) {
        TypedQuery<Customer> query = entityManager.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
        query.setParameter("email", loginRequest.getEmail());

        List<Customer> customers = query.getResultList();
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        }

        Customer customer = customers.get(0);
        if (!customer.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");
        return ResponseEntity.ok(new LoginResponse(token, "CUSTOMER", customer.getEmail()));
    }
}
