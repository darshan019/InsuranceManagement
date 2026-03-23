package com.internship.InsuranceManagement.auth.controller;

import com.internship.InsuranceManagement.auth.dto.AuthResponse;
import com.internship.InsuranceManagement.auth.dto.LoginRequest;
import com.internship.InsuranceManagement.auth.dto.RegisterRequest;
import com.internship.InsuranceManagement.auth.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // CUSTOMER REGISTER
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        AuthResponse response = authService.register(req);
        return ResponseEntity.ok(response);
    }

    // SMART LOGIN (Customer or Admin)
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        AuthResponse response = authService.login(req);
        return ResponseEntity.ok(response);
    }
}
