package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.ClaimDAO;
import com.internship.InsuranceManagement.entity.Claim;
import com.internship.InsuranceManagement.service.interfaces.ClaimService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimDAO claimDAO;

    @Autowired
    public ClaimServiceImpl(ClaimDAO ClaimDAO) {
        this.claimDAO = ClaimDAO;
    }


    @Override
    public List<Claim> findNotApproved() {
        return claimDAO.findNotApproved();
    }
    @Override
    public List<Claim> findAll() {
        return claimDAO.findAll();
    }

    @Override
    @Transactional
    public Claim save(Claim Claim) {
        return claimDAO.save(Claim);
    }

    @Override
    public Claim findById(int id) {
        return claimDAO.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        claimDAO.deleteById(id);
    }
}
