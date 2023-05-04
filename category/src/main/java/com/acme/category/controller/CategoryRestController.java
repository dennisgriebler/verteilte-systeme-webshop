package com.acme.category.controller;


import com.acme.category.buisnesslogic.impl.CategoryManagerImpl;
import com.acme.category.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CategoryRestController {

    private CategoryManagerImpl service;

    public CategoryRestController (CategoryManagerImpl service) {
        this.service = service;
    }

    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<Category> getUser(@PathVariable int categoryId) {
        Category category =  service.getCategory(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);}

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Category>> getCategories() {
        Iterable<Category> allCategories = service.getCategories();
        // TODO: Filter via Parameter?
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @RequestMapping(value = "/categories", method = RequestMethod.POST)
    public ResponseEntity<?> addCategory() {
        service.toString();
        return null;
    }

    @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity<Category> deleteCategory(@PathVariable Long categoryId) {
        boolean success = false; //service.deleteOne(categoryId);
        return null;
    }

}
