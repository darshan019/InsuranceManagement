package com.internship.InsuranceManagement.dto;

import com.internship.InsuranceManagement.entity.*;

public class DTOMapper {

    public static CustomerDTO toCustomerDTO(Customer c) {
        if (c == null) return null;
        return new CustomerDTO(c.getCustomerId(), c.getUsername(), c.getEmail(), c.getAddress(), c.getDateOfBirth());
    }

    public static AdminDTO toAdminDTO(Admin a) {
        if (a == null) return null;
        return new AdminDTO(a.getAdminId(), a.getUsername(), a.getEmail());
    }

    public static AgentDTO toAgentDTO(Agent a) {
        if (a == null) return null;
        return new AgentDTO(a.getAgentId(), a.getName(), a.getEmail(), a.getPhone());
    }

    public static PolicyTemplateDTO toPolicyTemplateDTO(PolicyTemplate pt) {
        if (pt == null) return null;
        return new PolicyTemplateDTO(
                pt.getTemplateId(),
                toAgentDTO(pt.getAgent()),
                pt.getInsuranceType(),
                pt.getCategory(),
                pt.getTemplateName(),
                pt.getPremiumAmount(),
                pt.getCoverageAmount()
        );
    }

    public static PolicyDTO toPolicyDTO(Policy p) {
        if (p == null) return null;
        return new PolicyDTO(
                p.getPolicyId(),
                toCustomerDTO(p.getCustomer()),
                toPolicyTemplateDTO(p.getTemplate()),
                p.getPolicyNumber(),
                p.getStartDate(),
                p.getEndDate(),
                p.getStatus()
        );
    }

    public static ClaimDTO toClaimDTO(Claim c) {
        if (c == null) return null;
        return new ClaimDTO(
                c.getClaimId(),
                toPolicyDTO(c.getPolicy()),
                c.getClaimDate(),
                c.getDescription(),
                c.getClaimAmount(),
                c.getStatus(),
                toAdminDTO(c.getApprovedBy()),
                c.getApprovedAt()
        );
    }

    public static PaymentDTO toPaymentDTO(Payment p) {
        if (p == null) return null;
        return new PaymentDTO(
                p.getPaymentId(),
                toCustomerDTO(p.getCustomer()),
                toPolicyDTO(p.getPolicy()),
                p.getPaymentDate(),
                p.getAmount(),
                p.getPaymentMethod(),
                p.getStatus()
        );
    }
}
