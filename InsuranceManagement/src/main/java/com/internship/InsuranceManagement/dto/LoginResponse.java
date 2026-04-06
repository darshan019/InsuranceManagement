package com.internship.InsuranceManagement.dto;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String role;
    private String email;


}
