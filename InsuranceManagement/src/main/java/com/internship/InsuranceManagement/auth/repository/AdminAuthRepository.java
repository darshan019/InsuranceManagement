package com.internship.InsuranceManagement.auth.repository;

import com.internship.InsuranceManagement.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminAuthRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmail(String email);
}