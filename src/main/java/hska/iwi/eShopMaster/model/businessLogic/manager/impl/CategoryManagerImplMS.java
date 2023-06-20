package hska.iwi.eShopMaster.model.businessLogic.manager.impl;


import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import java.util.List;
import java.util.ArrayList;

public class CategoryManagerImplMS implements CategoryManager{

	private static String url = "http://categories.default.svc.cluster.local:8890/";

	public List<Category> getCategories() {
		String input = RestCalls.makeGetRequest(url + "categories");
        if(!input.isEmpty()) {
            return deserializeCategories(input);
        }
        return new ArrayList<Category>();
	}

	public Category getCategory(int id) {
		String input = RestCalls.makeGetRequest(url + "categories/" + Integer.toString(id));
        if(!input.isEmpty()) {
            return deserializeCategory(input);
        }
        return null;
	}

	public Category getCategoryByName(String name) {
		String input = RestCalls.makeGetRequest(url + "categories/" + name);
        if(!input.isEmpty()) {
            return deserializeCategory(input);
        }
        return null;
	}

	public void addCategory(String name) {
        try{
			ObjectMapper mapper = new ObjectMapper();

            ObjectNode json = mapper.createObjectNode();
            json.put("name", name);
            String jsonString = mapper.writeValueAsString(json);
			System.out.println(jsonString);
            RestCalls.makePostRequest(url + "categories",jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }
	}

	public void delCategory(Category cat) {
// 		Products are also deleted because of relation in Category.java 
		delCategoryById(cat.getId());
	}

	public void delCategoryById(int id) {
		System.out.println(RestCalls.makeDeleteRequest(url + "categories/" + Integer.toString(id)));
	}

    private Category deserializeCategory(String json) {
        try{
		System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();

        Category category = objectMapper.readValue(json, Category.class);
        return category;
        } catch(Exception e) {
            System.out.println("Error 404");   
        }
        return null;
    }

    private List<Category> deserializeCategories(String json) {
        try{
		System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        String text = rootNode.get("_embedded").get("categoryList").toString();
		text = text.replaceAll("categoryId", "id");
        ArrayList<Category> categories = objectMapper.readValue(text, new TypeReference<List<Category>>(){});
        return categories;
        } catch(Exception e) {
            System.out.println("Error 404");   
            e.printStackTrace();
        }
        return new ArrayList<Category>();
    }
}
