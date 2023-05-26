package com.acme.product.repo;

import com.acme.product.model.Category;
import com.acme.product.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class LoadDatabase {
    /*
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private static Category category1 = new Category(1);
    private static Category category2 = new Category(2);


    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepo, CategoryRepository categoryRepo ) {
        return args -> {
            log.info("Preloading " + categoryRepo.save(category1));
            log.info("Preloading " + categoryRepo.save(category2));
            log.info("Preloading " + productRepo.save(new Product("Baby-Öl", 19.99, category1, "Interna...")));
            log.info("Preloading " + productRepo.save(new Product("Superman Kostüm", 89.99, category2, "Interna...")));
        };
    }*/
}
