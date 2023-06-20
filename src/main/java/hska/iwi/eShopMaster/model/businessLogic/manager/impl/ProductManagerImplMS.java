package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.impl.CategoryManagerImplMS;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.type.TypeReference;

import java.util.List;
import java.util.ArrayList;


public class ProductManagerImplMS implements ProductManager {
    private static String url = "http://products.default.svc.cluster.local:8889/";
	
	public List<Product> getProducts() {
        String input = RestCalls.makeGetRequest(url + "products");
        if(!input.isEmpty()) {
            return deserializeProducts(input);
        }
        return new ArrayList<Product>();

	}
	
	public List<Product> getProductsForSearchValues(String searchDescription,
			Double searchMinPrice, Double searchMaxPrice) {	
        String queryParts = "";
        if (searchDescription != null && !searchDescription.isEmpty()) {
            queryParts = queryParts.isEmpty() ? "searchDescription="+searchDescription : queryParts + "&" + "searchDescription="+searchDescription;  

        }
        if (searchMinPrice != null) {
            queryParts = queryParts.isEmpty() ? "searchMinPrice="+searchMinPrice : queryParts + "&" + "searchMinPrice="+searchMinPrice;  
        } 
        if (searchMaxPrice != null) {
            queryParts = queryParts.isEmpty() ? "searchMaxPrice="+searchMaxPrice : queryParts + "&" + "searchMaxPrice="+searchMaxPrice;  
        }
        
        String queryUrl = queryParts.isEmpty() ? url + "products" : url + "products?" + queryParts;
		String input = RestCalls.makeGetRequest(queryUrl);
        if (!input.isEmpty()) {
            return deserializeProducts(input);
        }
        return new ArrayList<Product>();
	}

	public Product getProductById(int id) {
        String input = RestCalls.makeGetRequest(url + "products/" + Integer.toString(id));
        if(!input.isEmpty()) {
            return deserializeProduct(input);
        }
        return null;
	}

	public Product getProductByName(String name) {
        //Warning Deprecated
		String input = RestCalls.makeGetRequest(url + "products/" + name);
        if(!input.isEmpty()) {
            return deserializeProduct(input);
        }
        return null;
	}
	
	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;
        try{
			ObjectMapper mapper = new ObjectMapper();
            ObjectNode json = mapper.createObjectNode();
            json.put("name", name);
            json.put("price", Double.toString(price));
            if(details != null){
            json.put("details", details);
            }

            ObjectNode category = mapper.createObjectNode();
            category.put("categoryId", categoryId);
            json.put("category", category);
            String jsonString = mapper.writeValueAsString(json);
            String response = RestCalls.makePostRequest(url + "products",jsonString);
            mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);
            productId = jsonNode.get("id").asInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
		return productId;
    }

	public void deleteProductById(int id) {
		System.out.println(RestCalls.makeDeleteRequest(url + "products/" + Integer.toString(id)));
	}

    public boolean deleteProductsByCategoryId(int id) {
        return false;
    }

    private Product deserializeProduct(String json) {
        try{
        ObjectMapper objectMapper = new ObjectMapper();

        Product product = objectMapper.readValue(json, Product.class);
        return product;
        } catch(Exception e) {
            System.out.println("Error 404");   
        }
        return null;
    }

    private List<Product> deserializeProducts(String json) {
        try{
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);
        String text = rootNode.get("_embedded").get("productList").toString();
        text = text.replaceAll("categoryId", "id");
        ArrayList<Product> products = objectMapper.readValue(text, new TypeReference<List<Product>>(){});
        for(int i = 0; i < products.size();i++) {
            Product currentProd = products.get(i);
            int catId = currentProd.getCategory().getId();
            currentProd.getCategory().setName(new CategoryManagerImplMS().getCategory(catId).getName());
        }
        return products;
        } catch(Exception e) {
            System.out.println("Error 404");   
            e.printStackTrace();
        }
        return new ArrayList<Product>();
    }
}
