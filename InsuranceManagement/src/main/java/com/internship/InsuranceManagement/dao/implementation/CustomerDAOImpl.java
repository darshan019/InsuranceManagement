package com.internship.InsuranceManagement.dao.implementation;

import com.internship.InsuranceManagement.dao.interfaces.CustomerDAO;
import com.internship.InsuranceManagement.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerDAOImpl implements CustomerDAO {
    private final EntityManager entityManager;

    @Autowired
    public CustomerDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Customer> findAll() {
        TypedQuery<Customer> query = entityManager.createQuery("from Customer", Customer.class);
        return query.getResultList();
    }

    @Override
    public Customer findById(int id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public Customer save(Customer customer) {
        return entityManager.merge(customer);
    }

    @Override
    public void deleteById(int id) {
        Customer customer = entityManager.find(Customer.class, id);
        entityManager.remove(customer);
    }

    @Override
    public Customer findByEmail(String email) {

        String q = "SELECT c FROM Customer c WHERE LOWER(c.email) = LOWER(:email)";

        List<Customer> list = entityManager.createQuery(q, Customer.class)
                .setParameter("email", email.trim())
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }
}
