package com.acme.product.repo;

import com.acme.product.model.Product;
import com.acme.product.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Optional;

@ResponseBody
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);

    Product[] findByCategory(int categoryId);

    int deleteProductById(int categoryId);

    int deleteByCategory(Optional<Category> category);

}
