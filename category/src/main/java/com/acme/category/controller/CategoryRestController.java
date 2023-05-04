package com.acme.category.controller;


import com.acme.category.buisnesslogic.impl.CategoryManagerImpl;
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

    @GetMapping(value = "/categories/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable int categoryId) {
        try {
            Category category = service.getCategory(categoryId);
            return new ResponseEntity<>(category, HttpStatus.OK);
        } catch (Exception e) {
            // TODO: Fall Kategorie nicht gefunden wird
            // TODO: Datenbank nicht erreichbar
        }
        return null;
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<Iterable<Category>> getCategories() {
        // Kein Filterparameter für categories
        // TODO: Filterparameter im Produkt Microservice müssen noch untersuchen und behandelt werden
        Iterable<Category> allCategories = service.getCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping(value = "/categories")
    public ResponseEntity<?> addCategory() {
        // TODO: Request Body auslesen
        // TODO: ggf. Fehlerbehandlung wenn Inputdaten unzureichend sind?
        // TODO: Fall Kategorie existiert schon => Exception: CategoryExistsException mit Nachricht: Kategorie schon vorhanden
        // service.toString();
        try {

        } catch(Exception e) {

        }
        return null;
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
