package com.acme.category.buisnesslogic.impl;


import com.acme.category.buisnesslogic.CategoryManager;
import com.acme.category.exception.CategoryExistsException;
import com.acme.category.exception.CategoryNotFoundException;
import com.acme.category.exception.InvalidCategoryNameException;
import com.acme.category.exception.CouldNotDeleteCategoryException;
import com.acme.category.repo.CategoryRepository;
import com.acme.category.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public Category getCategory(int id) throws CategoryNotFoundException {
		Optional<Category> category =  helper.findById((long)id);
		if (category.isPresent()) {
			return category.get();
		} else {
			throw new CategoryNotFoundException("Category with ID " + id + " not found");
		}
	}

	public Category getCategoryByName(String name) throws CategoryNotFoundException {
		//Iterable<Category> it = helper.findAll();
		//for (Category category : it) {
		//	if (category.getName().equals(name)) return category;
		//}
        Category category = helper.getCategoryByName(name);
        if (category == null) throw new CategoryNotFoundException("Category with name " + name + " not found");
        return category;
    }

    public boolean existsCategoryByName(String name) {
        try { getCategoryByName(name); }
        catch (CategoryNotFoundException e) { return false; }
        return true;
    }

    public Category addCategory(Category category) throws CategoryExistsException {
        return addCategory(category == null ? null : category.getName());
    }
	public Category addCategory(String name) throws CategoryExistsException {
        log.info("Categorie name: " + name);

        if (name == null || name.length() == 0)
            throw new InvalidCategoryNameException("A name is required for a new category.");

        if (existsCategoryByName(name)) {
            log.info("Categorie found: " + name);
            throw new CategoryExistsException(name);
        }

        Category category = new Category(name);
        return helper.save(category);
	}

	public void delCategory(Category cat) {

        int categoryId = cat.getId();

        RestTemplate restTemplate = new RestTemplate();

        String url = String.format("http://localhost:8090/deleteProductsByCategoryId/%d", categoryId);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Use of exchange due to further need for the HttpStatusCode, which is not obtained by delete
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();

        if (statusCode.equals(HttpStatus.NO_CONTENT)) {
            try {
                int deleteRecord = helper.deleteCategoryById(categoryId);
                log.info("Deleted category count: " + deleteRecord);
            }
            catch (Exception e) {
                log.info("Exception e: " + String.valueOf(e));
                throw new CouldNotDeleteCategoryException(String.valueOf(categoryId));
            }
        }
        else {
            throw new CouldNotDeleteCategoryException(String.valueOf(categoryId));
        }
	}

    @Transactional
	public void delCategoryById(int categoryId) {
		// TODO: Interkation mit Product Microservice

        RestTemplate restTemplate = new RestTemplate();

        String url = String.format("http://localhost:8090/deleteProductsByCategoryId/%d", categoryId);

        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Use of exchange due to further need for the HttpStatusCode, which is not obtained by delete
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();

        if (statusCode.equals(HttpStatus.NO_CONTENT)) {
            try {
                int deleteRecord = helper.deleteCategoryById(categoryId);
                log.info("Deleted category count: " + deleteRecord);
            }
            catch (Exception e) {
                log.info("Exception e: " + String.valueOf(e));
                throw new CouldNotDeleteCategoryException(String.valueOf(categoryId));
            }
        }
        else {
            throw new CouldNotDeleteCategoryException(String.valueOf(categoryId));
        }


	}
}
