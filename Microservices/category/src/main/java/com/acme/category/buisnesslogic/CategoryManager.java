package com.acme.category.buisnesslogic;

import com.acme.category.model.Category;

public interface CategoryManager {

	public Iterable<Category> getCategories();
	
	public Category getCategory(int id);
	
	public Category getCategoryByName(String name);
	
	public void addCategory(String name);
	
	public void delCategory(Category cat);
	
	public void delCategoryById(int id);

	
}
