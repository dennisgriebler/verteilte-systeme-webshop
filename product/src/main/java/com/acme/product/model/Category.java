package com.acme.product.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

/**
 * This class contains details about categories.
 */
@Entity
@Table(name = "category")
@JsonIgnoreProperties("id")
public class Category implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int id;

    @Column(name = "categoryId", nullable = false)
    private int categoryId;

	public Category() {}

	public Category(Integer id_external) {
		this.categoryId = id_external;
	}


	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        return categoryId == category.categoryId;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + categoryId;
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                '}';
    }
}
