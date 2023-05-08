package com.acme.category.repo;


import com.acme.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findCategoriesByNameContaining(String name);

    int deleteCategoryById(int categoryId);
}
