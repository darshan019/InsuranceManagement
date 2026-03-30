package com.internship.InsuranceManagement.dto;

import java.time.LocalDate;

public class CustomerDTO {
    private int customerId;
    private String username;
    private String email;
    private String address;
    private LocalDate dateOfBirth;

    public CustomerDTO() {}

    public CustomerDTO(int customerId, String username, String email, String address, LocalDate dateOfBirth) {
        this.customerId = customerId;
        this.username = username;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public int getCustomerId() {

        return customerId;
    }

    public void setCustomerId(int customerId) {

        this.customerId = customerId;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
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
}
