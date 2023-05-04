package com.acme.category.buisnesslogic.impl;



import com.acme.category.buisnesslogic.CategoryManager;
import com.acme.category.exception.CategoryExistsException;
import com.acme.category.repo.CategoryRepository;
import com.acme.category.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryManagerImpl implements CategoryManager {
	private CategoryRepository helper;
    private static final Logger log = LoggerFactory.getLogger(CategoryManagerImpl.class);


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
        return helper.getCategoryByName(name);
	}

	public void addCategory(String name) throws CategoryExistsException {
        log.info("Categorie name: " + name);
        Category category = getCategoryByName(name);
        log.info("Categorie found: " + category);

        if (category != null) throw new CategoryExistsException(category.getName());

        category = new Category(name);
        helper.save(category);
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
