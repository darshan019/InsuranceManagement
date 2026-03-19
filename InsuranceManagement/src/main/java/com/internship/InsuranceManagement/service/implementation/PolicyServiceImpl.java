package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.CustomerDAO;
import com.internship.InsuranceManagement.dao.interfaces.PolicyDAO;
import com.internship.InsuranceManagement.dao.interfaces.PolicyTemplateDAO;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.entity.PolicyTemplate;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {
    private final PolicyDAO policyDAO;
    private final CustomerDAO customerDAO;
    private final PolicyTemplateDAO policyTemplateDAO;

    @Autowired
    public PolicyServiceImpl(PolicyDAO policyDAO, CustomerDAO customerDAO, PolicyTemplateDAO policyTemplateDAO) {
        this.policyDAO = policyDAO;
        this.customerDAO = customerDAO;
        this.policyTemplateDAO = policyTemplateDAO;
    }

    @Override
    public List<Policy> findAll() {
        return policyDAO.findAll();
    }

    @Override
    @Transactional
    public Policy save(Policy policy) {
        return policyDAO.save(policy);
    }

    @Override
    public Policy findById(int id) {
        return policyDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        policyDAO.deleteById(id);
    }

    public List<Policy> findPoliciesOfAgent(int agentId) {
        return policyDAO.findPoliciesByAgentId(agentId);
    }

    @Override
    public List<Policy> findPoliciesByCustomerId(int customerId) {
        return policyDAO.findPoliciesByCustomerId(customerId);
    }

    @Override
    @Transactional
    public Policy buyPolicy(int customerId, int templateId) {
        Customer customer = customerDAO.findById(customerId);

        PolicyTemplate template = policyTemplateDAO.findById(templateId);

        Policy policy = new Policy();
        policy.setCustomer(customer);
        policy.setTemplate(template);
        policy.setPolicyNumber("POL-" + template.getInsuranceType().getTypeName().toUpperCase() + "-" + System.currentTimeMillis());
        policy.setStartDate(LocalDateTime.now());
        policy.setEndDate(LocalDateTime.now().plusYears(1));
        policy.setStatus("Active");

        return policyDAO.save(policy);
     }

    @Override
    @Transactional
    public Policy checkPolicyPayment(int policyId) {
        return policyDAO.checkPolicyPayment(policyId);
    }
}
