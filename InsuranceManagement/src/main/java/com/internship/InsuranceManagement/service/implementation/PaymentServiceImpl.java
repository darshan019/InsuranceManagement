package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.PaymentDAO;
import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.entity.Payment;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO;
    private final PolicyDAO policyDAO;

    @Autowired
    public PaymentServiceImpl(PaymentDAO paymentDAO, PolicyDAO policyDAO) {
        this.paymentDAO = paymentDAO;
        this.policyDAO = policyDAO;
    }

    @Override
    public List<Payment> findAll() {
        return paymentDAO.findAll();
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        // --- BUSINESS RULE ---
        // Premium can only be paid once per year per policy.
        // - If the policy's nextPremiumDate is in the future, reject.
        // - On success: mark policy ACTIVE and advance nextPremiumDate by 1 year
        //   from the ORIGINAL due date (not from today).

        if (payment.getPolicy() == null || payment.getPolicy().getPolicyId() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment must reference a valid policy.");
        }

        Policy policy = policyDAO.findById(payment.getPolicy().getPolicyId());
        if (policy == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Policy not found.");
        }

        String currentStatus = policy.getStatus() == null
                ? "" : policy.getStatus().toUpperCase();
        if (currentStatus.contains("CANCEL")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot pay premium on a cancelled policy.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextDue = policy.getNextPremiumDate();

        if (nextDue != null && nextDue.isAfter(now)) {
            // Not yet due — reject.
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You have already paid the premium for this term. "
                            + "Next premium is due on " + nextDue.toLocalDate() + ".");
        }

        // Save the payment
        Payment saved = paymentDAO.save(payment);

        // Advance the next premium date
        LocalDateTime newNextDue;
        if (nextDue == null) {
            // First ever payment — due in 1 year from today
            newNextDue = now.plusYears(1);
        } else {
            // Late payment — schedule from the ORIGINAL due date
            newNextDue = nextDue.plusYears(1);
            // Guard: if the customer was so late that even the new due date is
            // already in the past, keep advancing by years until we're in the future.
            while (newNextDue.isBefore(now)) {
                newNextDue = newNextDue.plusYears(1);
            }
        }

        policy.setStatus("ACTIVE");
        policy.setNextPremiumDate(newNextDue);
        policyDAO.save(policy);

        return saved;
    }

    @Override
    public Payment findById(int id) {
        return paymentDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        paymentDAO.deleteById(id);
    }

    @Override
    public List<Payment> findByCustomerId(int customerId) {
        return paymentDAO.findByCustomerId(customerId);
    }
}
