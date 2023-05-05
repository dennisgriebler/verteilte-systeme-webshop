package com.acme.product.repo;

import com.acme.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.ResponseBody;


@ResponseBody
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findByName(String name);
}
