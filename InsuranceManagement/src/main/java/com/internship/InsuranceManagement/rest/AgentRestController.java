package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Customer;
import com.internship.InsuranceManagement.service.interfaces.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
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
        public List<Agent> getAgents() {
            return agentService.findAll();
        }

        @GetMapping("/{agentId}")
        public Agent getAgent(@PathVariable int agentId){
            return agentService.findById(agentId);
        }

        @PostMapping("/")
        public Agent postAgent(@RequestBody Agent agent) {
            agent.setAgentId(0);
            return agentService.save(agent);
        }
        @DeleteMapping("/{agentId}")
        public void deleteAgent(@PathVariable int agentId) {
        agentService.deleteById(agentId);
    }

        @GetMapping("/{agentId}/customers")
        public List<Customer> getCustomers(@PathVariable int agentId) {
            return agentService.getCustomers(agentId);
        }



}
