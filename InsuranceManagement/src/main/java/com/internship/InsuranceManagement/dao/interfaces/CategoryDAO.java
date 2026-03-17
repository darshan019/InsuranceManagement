package com.internship.InsuranceManagement.dao.interfaces;

import com.internship.InsuranceManagement.entity.Category;

import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();
    Category findById(int id);
    Category save(Category category);
    void deleteById(int id);

}
