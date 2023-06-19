package hska.iwi.eShopMaster.model.businessLogic.manager.impl;

import hska.iwi.eShopMaster.model.businessLogic.manager.CategoryManager;
import hska.iwi.eShopMaster.model.businessLogic.manager.ProductManager;
import hska.iwi.eShopMaster.model.database.dataAccessObjects.ProductDAO;
import hska.iwi.eShopMaster.model.database.dataobjects.Category;
import hska.iwi.eShopMaster.model.database.dataobjects.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.util.List;
import java.util.ArrayList;


public class ProductManagerImplMS implements ProductManager {
    private static String url = "http://products.default.svc.cluster.local:8889/";
	private ProductDAO helper;
	
	public ProductManagerImplMS() {
		helper = new ProductDAO();
	}

	public List<Product> getProducts() {
        String input = makeGetRequest(url + "products");
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
        System.out.println("Constructed query url: "+queryUrl);
		String input = makeGetRequest(queryUrl);
        if (!input.isEmpty()) {
            return deserializeProducts(input);
        }
        return new ArrayList<Product>();
	}

	public Product getProductById(int id) {
        String input = makeGetRequest(url + "products/" + Integer.toString(id));
        if(!input.isEmpty()) {
            return deserializeProduct(input);
        }
        return null;
	}

	public Product getProductByName(String name) {
        //Warning Deprecated
		String input = makeGetRequest(url + "products/" + name);
        if(!input.isEmpty()) {
            return deserializeProduct(input);
        }
        return null;
	}
	
	public int addProduct(String name, double price, int categoryId, String details) {
		int productId = -1;
		
		CategoryManager categoryManager = new CategoryManagerImpl();
		Category category = categoryManager.getCategory(categoryId);
		
		if(category != null){
			Product product;
			if(details == null){
				product = new Product(name, price, category);	
			} else{
				product = new Product(name, price, category, details);
			}
			helper.saveObject(product);
			productId = product.getId();
		}
		return productId;
	}

	public void deleteProductById(int id) {
		helper.deleteById(id);
	}

	public boolean deleteProductsByCategoryId(int categoryId) {
		return false;
	}

    private String makeGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println("Response: " + response.toString());

            connection.disconnect();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return "";
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
        System.out.println(text);
        ArrayList<Product> products = objectMapper.readValue(text, new TypeReference<List<Product>>(){});
        return products;
        } catch(Exception e) {
            System.out.println("Error 404");   
            e.printStackTrace();
        }
        return new ArrayList<Product>();
    }
}
