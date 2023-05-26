package com.acme.product.controller;


import com.acme.product.buisnesslogic.impl.ProductManagerImpl;
import com.acme.product.exception.CategoryNotFound;
import com.acme.product.exception.DatabaseNotAvailableException;
import com.acme.product.exception.ProductNotFoundException;
import com.acme.product.exception.CouldNotDeleteProductException;
import com.acme.product.model.Product;
import com.acme.product.model.ProductModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class ProductRestController {

    private static final Logger log = LoggerFactory.getLogger(com.acme.product.controller.ProductRestController.class);


    private final ProductManagerImpl service;
    private final ProductModelAssembler assembler;

    // Kommunikation: Shared Kernel oder Consumer/Supplier
    public ProductRestController(ProductManagerImpl service, ProductModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFound.class)
    public ResponseEntity<String> handleProductNotFoundException(CategoryNotFound e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler(DatabaseNotAvailableException.class)
    public ResponseEntity<String> handleDatabaseNotAvailableException(DatabaseNotAvailableException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(CouldNotDeleteProductException.class)
    public ResponseEntity<String> CouldNotDeleteProductException(CouldNotDeleteProductException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable int productId) {
        Product product = service.getProductById(productId);
        return new ResponseEntity<>(assembler.toModel(product), HttpStatus.OK);
    }
    @GetMapping(value = "/products")
    public ResponseEntity<Iterable<?>> getProducts(@RequestParam(required = false, defaultValue = "", name = "searchDescription") String searchDescription,
                                                   @RequestParam(required = false, defaultValue = "",name = "searchMinPrice") Double searchMinPrice,
                                                   @RequestParam(required = false, defaultValue = "", name = "searchMaxPrice") Double searchMaxPrice) {
        log.info(searchDescription, searchMinPrice, searchMaxPrice);
        List<EntityModel<Product>> allProducts = StreamSupport
                .stream(service.getProductsForSearchValues(searchDescription, searchMinPrice, searchMaxPrice).spliterator(), false)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                CollectionModel.of(allProducts), HttpStatus.OK);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<?> addProduct(@RequestBody Product productForm) throws Exception {
        Product product = service.addProduct(productForm);
        return new ResponseEntity<>(assembler.toModel(product), HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable Integer productId) {
        service.deleteProductById(productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/deleteProductsByCategoryId/{categoryId}")
    public ResponseEntity<HttpStatus> deleteProductByCategory(@PathVariable Integer categoryId) {
        service.deleteProductsByCategoryId(categoryId);

        return ResponseEntity.noContent().build();

    }

}
