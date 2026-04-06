package com.internship.InsuranceManagement.dto;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private int customerId;
    private String username;
    private String email;
    private String address;
    private LocalDate dateOfBirth;


}
