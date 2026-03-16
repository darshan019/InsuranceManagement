package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Policy;
import com.internship.InsuranceManagement.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{agentId}/policies")
    public List<Policy> getPoliciesOfAgent(@PathVariable int agentId) {
        return policyService.findPoliciesOfAgent(agentId);
    }
}
