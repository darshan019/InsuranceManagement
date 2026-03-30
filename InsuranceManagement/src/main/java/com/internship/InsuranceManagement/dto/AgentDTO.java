package com.internship.InsuranceManagement.dto;

public class AgentDTO {
    private int agentId;
    private String name;
    private String email;
    private String phone;

    public AgentDTO() {}

    public AgentDTO(int agentId, String name, String email, String phone) {
        this.agentId = agentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getAgentId() {

        return agentId;
    }

    public void setAgentId(int agentId) {

        this.agentId = agentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
