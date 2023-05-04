package com.acme.category.controller;


import com.acme.category.buisnesslogic.impl.CategoryManagerImpl;
import com.acme.category.exception.CategoryExistsException;
import com.acme.category.exception.CategoryNotFoundException;
import com.acme.category.exception.DatabaseNotAvailableException;
import com.acme.category.exception.InvalidCategoryNameException;
import com.acme.category.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class CategoryRestController {

    private CategoryManagerImpl service;

    // Kommunikation: Shared Kernel oder Consumer/Supplier
    // TODO: GET => Pia
    // TODO: POST => Dennis
    // TODO: DELETE => Matthias
    public CategoryRestController (CategoryManagerImpl service) {
        this.service = service;
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

    @GetMapping(value = "/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable int categoryId) {
        Category category = service.getCategory(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<Iterable<Category>> getCategories() {
        // TODO: Methode hat eigentlich ein List<Category> zurück gegeben (ggf in impl des webshop anpassen
        // TODO: Filterparameter im Produkt Microservice müssen noch untersuchen und behandelt werden
        Iterable<Category> allCategories = service.getCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<?> addCategory(@RequestParam String name) {
        // TODO: Input als Pfad Param oder im Body?
        // TODO: Links bauen
        // Category category = null;
        service.addCategory(name);
        return new ResponseEntity<>(null, HttpStatus.CREATED);

    }

    @DeleteMapping(value = "/categories/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long categoryId) {
        // TODO: Fall Kategorie ID nicht vorhanden => Behandlung, Löschen war erfolgreich!
        // TODO: Produkt löschen via Produkt Microservice... => Transaktion?
        // TODO: Ist ein cascading delete in verteilten System mit Spring Werkzeugen möglich?
        boolean success = false; //service.deleteOne(categoryId);
        return null;
    }

}
