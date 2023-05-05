package com.acme.category.model;

import com.acme.category.controller.CategoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>> {
    @Override
    public EntityModel<Category> toModel(Category category) {

        return EntityModel.of(category,
                linkTo(methodOn(CategoryRestController.class).getCategory(category.getId())).withSelfRel(),
                linkTo(methodOn(CategoryRestController.class).getCategories()).withRel("categories"));
    }
}
