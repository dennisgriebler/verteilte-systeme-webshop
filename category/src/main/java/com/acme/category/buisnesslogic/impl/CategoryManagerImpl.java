package com.acme.category.buisnesslogic.impl;



import com.acme.category.buisnesslogic.CategoryManager;
import com.acme.category.repo.CategoryRepository;
import com.acme.category.model.Category;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryManagerImpl implements CategoryManager {
	private CategoryRepository helper;
	
	public CategoryManagerImpl(CategoryRepository helper) {
        this.helper = helper;
    }

	public Iterable<Category> getCategories() {
		return helper.findAll();
	}

	public Category getCategory(int id) {
		Optional<Category> category =  helper.findById((long)id);
        return category.orElse(null);
	}

	public Category getCategoryByName(String name) {
        // TODO: Impl
		return null;
	}

	public void addCategory(String name) {
		Category cat = new Category(name);
		helper.save(cat);
	}

	public void delCategory(Category cat) {

    // TODO: Products are also deleted because of relation in Category.java
		//helper.deleteById(cat.getId());
	}

	public void delCategoryById(int id) {
		// TODO: Interkation mit Product Microservice
        helper.deleteById((long) id);
	}
}
