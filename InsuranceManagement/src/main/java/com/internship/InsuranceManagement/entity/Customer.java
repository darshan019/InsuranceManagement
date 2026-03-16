package com.internship.InsuranceManagement.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String address;

    private LocalDate dateOfBirth;

    public Customer() {
    }

    public Customer(String username, String password, String email, String address, LocalDate dateOfBirth) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
