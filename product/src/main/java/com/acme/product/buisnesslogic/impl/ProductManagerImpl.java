package com.acme.product.buisnesslogic.impl;

import java.util.*;

import com.acme.product.buisnesslogic.ProductManager;
import com.acme.product.exception.CategoryNotFound;
import com.acme.product.exception.ProductNotFoundException;
import com.acme.product.exception.CouldNotDeleteProductException;
import com.acme.product.model.Category;
import com.acme.product.model.Product;
import com.acme.product.repo.CategoryRepository;
import com.acme.product.repo.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductManagerImpl implements ProductManager {

    private static final Logger log = LoggerFactory.getLogger(ProductManagerImpl.class);

    private ProductRepository productRepo;
    private CategoryRepository categoryRepo;

    public ProductManagerImpl(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Product> getProducts() {
        return productRepo.findAll();
    }

    public List<Product> getProductsForSearchValues(String searchDescription, Double searchMinPrice, Double searchMaxPrice) throws ProductNotFoundException {
        if (searchDescription == null) searchDescription = "";
        if (searchMinPrice == null) searchMinPrice = 0.0;
        if (searchMaxPrice == null) searchMaxPrice = Double.MAX_VALUE;

        List<Product> products = productRepo.findProductsByDetailsContainingAndPriceIsGreaterThanEqualAndPriceIsLessThanEqual(searchDescription, searchMinPrice, searchMaxPrice);
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException("No products found");
        }
    }

    public Product getProductById(int id) throws ProductNotFoundException {
        Optional<Product> product = productRepo.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
    }

    public List<Product> getProductsByName(String name) throws ProductNotFoundException {
        //Name not unique. Thus, method returns multiple products.
        if (name == null) name = "";
        List<Product> products = productRepo.findAllByName(name);
        if (!products.isEmpty()) {
            return products;
        } else {
            throw new ProductNotFoundException("No products found");
        }
    }

    public Product addProduct(Product product) throws CategoryNotFound {
        log.info("product: " + product);
        Category category = product.getCategory();
        return addProduct(product.getName(), product.getPrice(), category == null ? 0 : category.getCategoryId(), product.getDetails());
        //return product;
    }

    public Product addProduct(String name, double price, int categoryId, String details) throws CategoryNotFound {
        log.info("name=" + name + "; categoryId=" + categoryId, "; details=" + details);
        try {
            // Kategorie Microservice nach der Kategorie befragen
            //Map<String, Integer> params = new HashMap<>();
            //params.put("categoryId", categoryId);
            String url = String.format("http://categories.default.svc.cluster.local:8890/categories/%d", categoryId);
            ResponseEntity<Category> response
                    = new RestTemplate().getForEntity(
                    url,
                    Category.class);

            log.info("response body from category service=" + response.getBody().toString());

            Category category = response.getBody();
            Optional<Category> retrieval = categoryRepo.findByCategoryId(category.getCategoryId());
            //
            // Existiert die Kategorie schon? Nein, dann erst persistieren
            if (retrieval.isEmpty()) {
                log.info("save category to db");
                category = categoryRepo.save(category);
            } else {
                log.info("category was already in db");
                category = retrieval.get();
            }
            Product product;
            if (details == null) {
                product = new Product(name, price, category);
            } else {
                product = new Product(name, price, category, details);
            }

            Product saved = productRepo.save(product);
            log.info("saved product: " + saved);
            return saved;

        } catch (RestClientException ex) {
            throw new CategoryNotFound(categoryId);
        }
    }

    @Transactional
    public void deleteProductById(int categoryId) {
        try {
            int deletedRecord = productRepo.deleteProductById(categoryId);
            log.info("Deleted product count: " + deletedRecord);
        } catch (Exception e) {
            throw new CouldNotDeleteProductException(String.valueOf(categoryId));
        }
    }

    @Transactional
    public void deleteProductsByCategoryId(int categoryId) {
        try {
            Optional<Category> category = categoryRepo.findByCategoryId(categoryId);
            int deletedRecords = productRepo.deleteByCategory(category);
            log.info("Deleted product count: " + deletedRecords);
        } catch (Exception e) {
            throw new CouldNotDeleteProductException(String.valueOf(categoryId));
        }
    }

}
