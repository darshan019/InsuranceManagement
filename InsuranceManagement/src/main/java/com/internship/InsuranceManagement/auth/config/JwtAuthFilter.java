package com.internship.InsuranceManagement.auth.config;

import com.internship.InsuranceManagement.auth.repository.CustomerAuthRepository;
import com.internship.InsuranceManagement.auth.repository.AdminAuthRepository;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.Admin;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomerAuthRepository customerRepo;
    private final AdminAuthRepository adminRepo;

    public JwtAuthFilter(JwtUtil jwtUtil,
                         CustomerAuthRepository customerRepo,
                         AdminAuthRepository adminRepo) {
        this.jwtUtil = jwtUtil;
        this.customerRepo = customerRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // NO TOKEN → move to next filter (permit /auth routes)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        // If not logged in already & token email not null
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (!jwtUtil.isValid(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            Object userDetails = null;

            // Check CUSTOMER first
            var customerOpt = customerRepo.findByEmail(email);
            if (customerOpt.isPresent()) {
                userDetails = customerOpt.get();
            }

            // Check ADMIN second
            var adminOpt = adminRepo.findByEmail(email);
            if (adminOpt.isPresent()) {
                userDetails = adminOpt.get();
            }

            // If user exists → set authentication
            if (userDetails != null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,    // <<<<<< MUST BE EMAIL, NOT user object
                                null,
                                List.of(authority)
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
