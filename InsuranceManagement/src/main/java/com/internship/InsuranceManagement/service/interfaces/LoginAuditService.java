package com.internship.InsuranceManagement.service.interfaces;

public interface LoginAuditService {

    void logEvent(String userType, int userId, String email, String action);
}
