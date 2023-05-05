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

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
	private int id;

    @Column(name = "categoryId", nullable = false)
    private int categoryId;

    //@Column(name = "name", nullable = false)
    //private String name;

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

    /*
	public void setName(int id_external) {
		this.externalId = id_external;
	}


    public String getName() {
        return name;
    }
     */

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                '}';
    }
}
