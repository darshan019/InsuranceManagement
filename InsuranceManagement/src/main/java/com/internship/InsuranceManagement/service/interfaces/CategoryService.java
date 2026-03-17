package com.internship.InsuranceManagement.service.interfaces;

import com.internship.InsuranceManagement.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category findById(int id);
    Category save(Category category);
    void deleteById(int id);
}
