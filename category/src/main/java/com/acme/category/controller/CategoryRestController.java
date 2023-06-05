package com.acme.category.controller;


import com.acme.category.buisnesslogic.impl.CategoryManagerImpl;
import com.acme.category.exception.CategoryExistsException;
import com.acme.category.exception.CategoryNotFoundException;
import com.acme.category.exception.DatabaseNotAvailableException;
import com.acme.category.exception.InvalidCategoryNameException;
import com.acme.category.exception.CouldNotDeleteCategoryException;
import com.acme.category.model.Category;
import com.acme.category.model.CategoryModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class CategoryRestController {

    private static final Logger log = LoggerFactory.getLogger(CategoryRestController.class);
    private java.util.Random random;

    private int randNum;

    private CategoryManagerImpl service;

    private final CategoryModelAssembler assembler;

    // Kommunikation: Shared Kernel oder Consumer/Supplier
    public CategoryRestController(CategoryManagerImpl service, CategoryModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
        random = new java.util.Random();
        randNum = random.nextInt(1000);
        System.out.println(Integer.toString(randNum));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DatabaseNotAvailableException.class)
    public ResponseEntity<String> handleDatabaseNotAvailableException(DatabaseNotAvailableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({InvalidCategoryNameException.class, CategoryExistsException.class})
    public ResponseEntity<String> handleCategoryAddExceptions(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(CouldNotDeleteCategoryException.class)
    public ResponseEntity<String> handleCouldNotDeleteCategoryException(CouldNotDeleteCategoryException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/categories/{categoryId}")
    public ResponseEntity<?> getCategory(@PathVariable int categoryId) {
        log.info("Find category with id=" + categoryId);
        Category category = service.getCategory(categoryId);
        log.info("Found category with id=" + category);
        HttpHeaders headers = new HttpHeaders();
        headers.add( "randomNum",Integer.toString(randNum));
        return new ResponseEntity<>(assembler.toModel(category), headers,HttpStatus.OK);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<Iterable<?>> getCategories(@RequestParam(required = false, name = "name") String name) {
        List<EntityModel<Category>> allCategories = StreamSupport.stream(service.getCategoriesByName(name).spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        HttpHeaders headers = new HttpHeaders();
        headers.add( "randomNum",Integer.toString(randNum));
        return new ResponseEntity<>(
                CollectionModel.of(allCategories, linkTo(methodOn(CategoryRestController.class)
                        .getCategories(name)).withSelfRel()), headers,HttpStatus.OK);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<?> addCategory(@RequestBody Category categoryForm) {
        Category category = service.addCategory(categoryForm);
        HttpHeaders headers = new HttpHeaders();
        headers.add( "randomNum",Integer.toString(randNum));
        return new ResponseEntity<>(assembler.toModel(category), headers, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Integer categoryId) {
        service.delCategoryById(categoryId);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.noContent().build();
    }

}
