package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.dto.DTOMapper;
import com.internship.InsuranceManagement.dto.PolicyDTO;
import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PolicyRestController {
    private final PolicyService policyService;

    @Autowired
    public PolicyRestController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policies")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<PolicyDTO> getPolicies() {
        return policyService.findAll().stream().map(DTOMapper::toPolicyDTO).toList();
    }

    @GetMapping("/agent/{agentId}/policies")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public List<PolicyDTO> getPoliciesOfAgent(@PathVariable int agentId) {
        return policyService.findPoliciesOfAgent(agentId).stream().map(DTOMapper::toPolicyDTO).toList();
    }

    @GetMapping("/customer/{customerId}/policies")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public List<PolicyDTO> getPoliciesOfCustomer(@PathVariable int customerId) {
        return policyService.findPoliciesByCustomerId(customerId).stream().map(DTOMapper::toPolicyDTO).toList();
    }

    @GetMapping("/policies/{policyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT', 'CUSTOMER')")
    public PolicyDTO getPolicy(@PathVariable int policyId) {
        return DTOMapper.toPolicyDTO(policyService.findById(policyId));
    }

    @PostMapping("/policies")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public Policy postPolicy(@RequestBody Policy policy) {
        policy.setPolicyId(0);
        return policyService.save(policy);
    }

    @PatchMapping("/policy/update/{policyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
    public PolicyDTO updatePolicyPayment(@PathVariable int policyId) {
        return DTOMapper.toPolicyDTO(policyService.checkPolicyPayment(policyId));
    }

    @DeleteMapping("/policies/{policyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePolicy(@PathVariable int policyId) {
        policyService.deleteById(policyId);
    }
}
