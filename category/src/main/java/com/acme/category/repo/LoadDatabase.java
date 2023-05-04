package com.acme.category.repo;

import com.acme.category.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CategoryRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Category("Käse")));
            log.info("Preloading " + repository.save(new Category("Früchte")));
        };
    }
}
