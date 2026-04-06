package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.LoginAuditDAO;
import com.internship.InsuranceManagement.entity.LoginAudit;
import com.internship.InsuranceManagement.service.interfaces.LoginAuditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginAuditServiceImpl implements LoginAuditService {

    private final LoginAuditDAO loginAuditDAO;

    public LoginAuditServiceImpl(LoginAuditDAO loginAuditDAO) {
        this.loginAuditDAO = loginAuditDAO;
    }

    @Override
    public void logEvent(String userType, int userId, String email, String action) {

        LoginAudit audit = new LoginAudit(
                0,
                userType,
                userId,
                email,
                action,
                LocalDateTime.now()
        );

        loginAuditDAO.save(audit);
    }
}