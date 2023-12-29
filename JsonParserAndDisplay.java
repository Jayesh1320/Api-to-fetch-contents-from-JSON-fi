import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;

class Product {
    String title;
    double price;
    int popularity;

    public Product(String title, double price, int popularity) {
        this.title = title;
        this.price = price;
        this.popularity = popularity;
    }
}

public class JsonParserAndDisplay {

    public static void main(String[] args) {
        String apiUrl = "https://s3.amazonaws.com/open-to-cors/assignment.json";
        try {
            String jsonData = fetchDataFromApi(apiUrl);
            if (jsonData != null) {
                List<Product> productList = parseJsonData(jsonData);
                displayProducts(productList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String fetchDataFromApi(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    private static List<Product> parseJsonData(String jsonData) {
        List<Product> productList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonData);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonProduct = jsonArray.getJSONObject(i);
            String title = jsonProduct.getString("title");
            double price = jsonProduct.getDouble("price");
            int popularity = jsonProduct.getInt("popularity");

            productList.add(new Product(title, price, popularity));
        }

        
        Collections.sort(productList, Comparator.comparingInt(Product::getPopularity).reversed());

        return productList;
    }

    private static void displayProducts(List<Product> productList) {
        System.out.println("Products ordered by descending popularity:");

        for (Product product : productList) {
            System.out.println("Title: " + product.title + " | Price: $" + product.price + " | Popularity: " + product.popularity);
        }
    }
}
