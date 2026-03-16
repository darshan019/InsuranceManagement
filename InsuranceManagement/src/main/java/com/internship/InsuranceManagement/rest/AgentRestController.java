package com.internship.InsuranceManagement.rest;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    @RestController
    @RequestMapping("/api")
    public class AgentRestController {
        private final AgentService agentService;

        @Autowired
        public AgentRestController(AgentService agentService) {
            this.agentService = agentService;
        }

        @GetMapping("/agents")
        public List<Agent> getAgents() {
            return agentService.findAll();
        }

}
