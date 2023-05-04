package com.acme.category.buisnesslogic.impl;



import com.acme.category.buisnesslogic.CategoryManager;
import com.acme.category.exception.CategoryExistsException;
import com.acme.category.exception.InvalidCategoryNameException;
import com.acme.category.repo.CategoryRepository;
import com.acme.category.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
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
		if (category.isPresent()) {
			return category.get();
		} else {
			throw new NoSuchElementException("Category with ID " + id + " not found");
		}
	}

	public Category getCategoryByName(String name) throws NoSuchElementException {
		//Iterable<Category> it = helper.findAll();
		//for (Category category : it) {
		//	if (category.getName().equals(name)) return category;
		//}
        Category category = helper.getCategoryByName(name);
        if (category == null) throw new NoSuchElementException("Category with name " + name + " not found");
        return category;
    }

    public boolean existsCategoryByName(String name) {
        try {
            getCategoryByName(name);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

	public void addCategory(String name) throws CategoryExistsException {
        log.info("Categorie name: " + name);

        if (name == null || name.length() == 0)
            throw new InvalidCategoryNameException("A name the the category is required.");

        if (existsCategoryByName(name)) {
            log.info("Categorie found: " + name);
            throw new CategoryExistsException(name);
        }

        Category category = new Category(name);
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
