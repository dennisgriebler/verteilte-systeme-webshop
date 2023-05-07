package com.acme.product.buisnesslogic;


import com.acme.product.model.Product;
import java.util.List;

public interface ProductManager {

	public List<Product> getProducts();

	public Product getProductById(int id);

	public Product getProductByName(String name);

	public Product addProduct(String name, double price, int categoryId, String details) throws Exception;

	public List<Product> getProductsForSearchValues(String searchValue, Double searchMinPrice, Double searchMaxPrice);

	public void deleteProductsByCategoryId(int categoryId);
	
    public void deleteProductById(int id);
	
}
