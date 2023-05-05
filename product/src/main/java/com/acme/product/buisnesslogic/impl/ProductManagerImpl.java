package com.acme.product.buisnesslogic.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.acme.product.buisnesslogic.ProductManager;
import com.acme.product.exception.CategoryNotFound;
import com.acme.product.exception.ProductNotFoundException;
import com.acme.product.model.Category;
import com.acme.product.model.Product;
import com.acme.product.repo.CategoryRepository;
import com.acme.product.repo.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    // TODO: Suche mit min-Preis, max-Preis, Beschreibung
	public List<Product> getProductsForSearchValues(String searchDescription, Double searchMinPrice, Double searchMaxPrice) {
		//return new ProductDAO().getProductListByCriteria(searchDescription, searchMinPrice, searchMaxPrice);
        /* Im Monolithen: ProductDao.java
        // Define Search HQL:
			if (searchDescription != null && searchDescription.length() > 0)
			{	// searchValue is set:
				searchDescription = "%"+searchDescription+"%";
				crit.add(Restrictions.ilike("details", searchDescription ));
			}

			if (( searchMinPrice != null) && ( searchMaxPrice != null)) {
					crit.add(Restrictions.between("price", searchMinPrice, searchMaxPrice));
				}
			else 	if( searchMinPrice != null) {
					crit.add(Restrictions.ge("price", searchMinPrice));
					}
			else if ( searchMaxPrice != null) {
					crit.add(Restrictions.le("price", searchMaxPrice));
			}

         */
        return null;
	}

	public Product getProductById(int id) throws ProductNotFoundException {
        Optional<Product> product =  productRepo.findById(id);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ProductNotFoundException("Product with ID " + id + " not found");
        }
	}

	public Product getProductByName(String name) {
        // Eins oder mehrere? Ist der Name unique?
		return productRepo.findByName(name);
	}

    public Product addProduct(Product product) throws CategoryNotFound {
        log.info("product: " + product);
        Category category = product.getCategory();
        return addProduct(product.getName(), product.getPrice(), category == null ? 0 : category.getCategoryId(), product.getDetails());
        //return product;
    }

    // TODO: Aufruf von Kategorie Microservice
	public Product addProduct(String name, double price, int categoryId, String details) throws CategoryNotFound {
		//int productId = -1;

        log.info("name=" + name + "; categoryId=" + categoryId, "; details=" + details);
        try {
            // Kategorie Microservice nach der Kategorie befragen
            Map<String, Integer> params = new HashMap<>();
            params.put("categoryId", categoryId);
            ResponseEntity<Category> response
                    = new RestTemplate().getForEntity(
                    "http://localhost:8080/categories/{categoryId}",
                    Category.class, params);

            log.info("response body from category service="+ response.getBody().toString());

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
            if(details == null){
                product = new Product(name, price, category);
            } else{
                product = new Product(name, price, category, details);
            }

            Product saved = productRepo.save(product);
            log.info("saved product: " + saved);
            return saved;

        }
        catch (RestClientException ex) {
            throw new CategoryNotFound(categoryId);
        }
	}
	

	public void deleteProductById(int id) {
		productRepo.deleteById(id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return false;
	}

}
