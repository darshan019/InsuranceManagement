package com.internship.InsuranceManagement.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // disable csrf for APIs
        http.csrf(csrf -> csrf.disable());

        // add JWT filter before Spring Security login filter
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // authorization rules
        http.authorizeHttpRequests(auth -> auth

                // public routes
                .requestMatchers("/api/auth/**").permitAll()   // Login + Register

                // admin-only routes
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // all BUSINESS APIs (customer, policy, claim, payment...)
                .requestMatchers("/api/**").hasAnyRole("USER","ADMIN")

                // everything else need authentication
                .anyRequest().authenticated()
        );

        return http.build();
    }
}