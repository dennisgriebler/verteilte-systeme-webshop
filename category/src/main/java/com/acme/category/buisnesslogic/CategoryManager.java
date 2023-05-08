package com.acme.category.buisnesslogic;

import com.acme.category.model.Category;

import java.util.List;

public interface CategoryManager {

	public Iterable<Category> getCategories();
	
	public Category getCategory(int id);
	
	public List<Category> getCategoriesByName(String name);
	
	public Category addCategory(String name);
	
	public void delCategory(Category cat);
	
	public void delCategoryById(int id);

	
}
