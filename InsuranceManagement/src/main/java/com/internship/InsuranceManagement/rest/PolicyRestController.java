package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.interfaces.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Policy> getPolicies() {
        return policyService.findAll();
    }

    @GetMapping("/agent/{agentId}/policies")
    public List<Policy> getPoliciesOfAgent(@PathVariable int agentId) {
        return policyService.findPoliciesOfAgent(agentId);
    }
    @GetMapping("/customer/{customerId}/policies")
    public List<Policy> getPoliciesOfCustomer(@PathVariable int customerId) {
        return policyService.findPoliciesByCustomerId(customerId);
    }

    @GetMapping("/policies/{policyId}")
    public Policy getPolicy(@PathVariable int policyId) {
        return policyService.findById(policyId);
    }

    @PostMapping("/policies")
    public Policy postPolicy(@RequestBody Policy policy) {
        policy.setPolicyId(0);
        return policyService.save(policy);
    }

    @PatchMapping("/policy/update/{policyId}")
    public Policy updatePolicyPayment(@PathVariable int policyId) {
        return policyService.checkPolicyPayment(policyId);
    }

    @DeleteMapping("/policies/{policyId}")
    public void deletePolicy(@PathVariable int policyId) {
        policyService.deleteById(policyId);
    }
}
