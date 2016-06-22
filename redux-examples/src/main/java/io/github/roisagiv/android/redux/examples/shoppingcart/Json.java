package io.github.roisagiv.android.redux.examples.shoppingcart;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Json {
  public static JSONArray toJson(List<ShoppingCartState.Product> products) throws JSONException {
    JSONArray array = new JSONArray();

    for (ShoppingCartState.Product product : products) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", product.getId());
      jsonObject.put("title", product.getTitle());
      jsonObject.put("price", product.getPrice());
      jsonObject.put("inventory", product.getInventory());

      array.put(jsonObject);
    }

    return array;
  }

  public static List<ShoppingCartState.Product> fromJson(JSONArray array) throws JSONException {
    List<ShoppingCartState.Product> products = new ArrayList<>();

    for (int i = 0; i < array.length(); i++) {
      JSONObject jsonObject = array.getJSONObject(i);
      ShoppingCartState.Product product = new ShoppingCartState.Product();

      product.setId(jsonObject.getInt("id"));
      product.setTitle(jsonObject.getString("title"));
      product.setPrice(jsonObject.getDouble("price"));
      product.setInventory(jsonObject.getInt("inventory"));

      products.add(product);
    }

    return products;
  }
}
