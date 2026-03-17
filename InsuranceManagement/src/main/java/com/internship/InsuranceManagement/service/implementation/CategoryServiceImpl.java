package com.internship.InsuranceManagement.service.implementation;

import com.internship.InsuranceManagement.dao.interfaces.CategoryDAO;
import com.internship.InsuranceManagement.entity.Category;
import com.internship.InsuranceManagement.service.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;
    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Category findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryDAO.save(category);
    }

    @Override
    public void deleteById(int id) {
         categoryDAO.deleteById(id);
    }
}
