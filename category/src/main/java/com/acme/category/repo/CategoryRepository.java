package com.acme.category.repo;


import com.acme.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryByName(String name);

    int deleteCategoryById(int categoryId);
}
