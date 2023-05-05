package com.acme.category.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name="ID")
    private Integer id;

    //@Column(name="NAME")
    private String name;

    public Category() { }

    public Category(String name) {
        this.name = name;
    }

    /*
    public Category(String name, Set<Integer> products) {
        this.name = name;
        this.products = products;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("categoryId")
    public Integer getId() {
        return id;
    }

    @JsonProperty("categoryId")
    public void setId(int id) { this.id = id;}


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Category))
            return false;
        Category employee = (Category) o;
        return Objects.equals(this.id, employee.id) && Objects.equals(this.name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    public String toString() {
        return "Category {" + "name=" + this.name + "}";
    }

}