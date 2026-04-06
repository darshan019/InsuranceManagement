package com.internship.InsuranceManagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "login_audit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auditId;

    @Column(nullable = false)
    private String userType;   // ADMIN / AGENT / CUSTOMER

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String action;     // LOGIN or LOGOUT

    @Column(nullable = false)
    private LocalDateTime actionTime;
}