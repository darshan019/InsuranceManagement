package com.internship.InsuranceManagement.auth.repository;

import com.internship.InsuranceManagement.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentAuthRepository extends JpaRepository<Agent, Integer> {
    Optional<Agent> findByEmail(String email);
}