package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.dto.LoginRequest;
import com.internship.InsuranceManagement.dto.LoginResponse;
import com.internship.InsuranceManagement.entity.Admin;
import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.security.JwtUtil;
import com.internship.InsuranceManagement.service.interfaces.LoginAuditService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EntityManager entityManager;
    private final JwtUtil jwtUtil;
    private final LoginAuditService loginAuditService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AuthController(EntityManager entityManager,
                          JwtUtil jwtUtil,
                          LoginAuditService loginAuditService,  PasswordEncoder passwordEncoder) {
        this.entityManager = entityManager;
        this.jwtUtil = jwtUtil;
        this.loginAuditService = loginAuditService;
        this.passwordEncoder = passwordEncoder;
    }


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
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password");
        }



        loginAuditService.logEvent(
                "ADMIN",
                admin.getAdminId(),
                admin.getEmail(),
                "LOGIN"
        );

        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
        return ResponseEntity.ok(new LoginResponse(token, "ADMIN", admin.getEmail(), admin.getAdminId()));
    }




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
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password");
        }



        loginAuditService.logEvent(
                "AGENT",
                agent.getAgentId(),
                agent.getEmail(),
                "LOGIN"
        );

        String token = jwtUtil.generateToken(agent.getEmail(), "AGENT");
        return ResponseEntity.ok(new LoginResponse(token, "AGENT", agent.getEmail(), agent.getAgentId()));
    }




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
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                customer.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password");
        }

        loginAuditService.logEvent(
                "CUSTOMER",
                customer.getCustomerId(),
                customer.getEmail(),
                "LOGIN"
        );

        String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");
        return ResponseEntity.ok(new LoginResponse(token, "CUSTOMER", customer.getEmail(), customer.getCustomerId()));
    }



    private int getUserIdByRoleAndEmail(String role, String email) {

        if ("ADMIN".equals(role)) {
            return entityManager.createQuery(
                            "SELECT a.adminId FROM Admin a WHERE a.email = :email",
                            Integer.class
                    ).setParameter("email", email)
                    .getSingleResult();
        }

        if ("AGENT".equals(role)) {
            return entityManager.createQuery(
                            "SELECT a.agentId FROM Agent a WHERE a.email = :email",
                            Integer.class
                    ).setParameter("email", email)
                    .getSingleResult();
        }

        return entityManager.createQuery(
                        "SELECT c.customerId FROM Customer c WHERE c.email = :email",
                        Integer.class
                ).setParameter("email", email)
                .getSingleResult();
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        String email = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);

        int userId = getUserIdByRoleAndEmail(role, email);

        loginAuditService.logEvent(
                role,
                userId,
                email,
                "LOGOUT"
        );

        return ResponseEntity.ok("Logout successful");
    }

}
