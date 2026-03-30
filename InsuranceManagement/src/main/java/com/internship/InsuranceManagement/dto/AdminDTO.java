package com.internship.InsuranceManagement.dto;

public class AdminDTO {
    private int adminId;
    private String username;
    private String email;

    public AdminDTO() {}

    public AdminDTO(int adminId, String username, String email) {
        this.adminId = adminId;
        this.username = username;
        this.email = email;
    }

    public int getAdminId() {

        return adminId;
    }

    public void setAdminId(int adminId) {

        this.adminId = adminId;
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
}
