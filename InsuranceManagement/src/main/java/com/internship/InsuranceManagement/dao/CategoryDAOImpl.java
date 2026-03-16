package com.internship.InsuranceManagement.dao;

import com.internship.InsuranceManagement.entity.Agent;
import com.internship.InsuranceManagement.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDAOImpl implements CategoryDAO{
    private final EntityManager entityManager;

    @Autowired
    public CategoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Category> findAll() {
        TypedQuery<Category> query = entityManager.createQuery("from Category", Category.class);
        return query.getResultList();
    }

    @Override
    public Category findById(int id) {
        return entityManager.find(Category.class,id);
    }

    @Override
    public Category save(Category category) {
        return entityManager.merge(category);
    }

    @Override
    public void deleteById(int id) {
        Category category = entityManager.find(Category.class, id);
        entityManager.remove(category);

    }
}
