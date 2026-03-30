package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.dto.CustomerDTO;
import com.internship.InsuranceManagement.service.interfaces.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
    @RequestMapping("/api/agents")
    public class AgentRestController {
        private final AgentService agentService;

        @Autowired
        public AgentRestController(AgentService agentService) {
            this.agentService = agentService;
        }

        @GetMapping("/")
        @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
        public List<Agent> getAgents() {
            return agentService.findAll();
        }

        @GetMapping("/{agentId}")
        @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
        public Agent getAgent(@PathVariable int agentId){
            return agentService.findById(agentId);
        }

        @PostMapping("/")
        @PreAuthorize("hasRole('ADMIN')")
        public Agent postAgent(@RequestBody Agent agent) {
            agent.setAgentId(0);
            return agentService.save(agent);
        }

        @DeleteMapping("/{agentId}")
        @PreAuthorize("hasRole('ADMIN')")
        public void deleteAgent(@PathVariable int agentId) {
            agentService.deleteById(agentId);
        }

        @GetMapping("/{agentId}/customers")
        @PreAuthorize("hasAnyRole('ADMIN', 'AGENT')")
        public List<CustomerDTO> getCustomers(@PathVariable int agentId) {
            List<Customer> customers = agentService.getCustomers(agentId);
            return customers.stream()
                .map(c -> new CustomerDTO(
                    c.getCustomerId(),
                    c.getUsername(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getDateOfBirth()
                ))
                .toList();
        }



}
