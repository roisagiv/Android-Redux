package io.roisagiv.android.redux.examples.shoppingcart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartSelectors {

  public static List<ShoppingCartState.Product> getProductsInCart(ShoppingCartState state) {
    List<ShoppingCartState.Product> results = new ArrayList<>();

    List<Integer> addedIds = state.getCart().getAddedIds();
    for (Integer addedId : addedIds) {
      results.add(state.getProducts().getById().get(addedId));
    }

    return results;
  }

  public static double getCartTotal(ShoppingCartState state) {
    double total = 0;
    List<Integer> addedIds = state.getCart().getAddedIds();
    for (Integer addedId : addedIds) {
      ShoppingCartState.Product product = state.getProducts().getById().get(addedId);
      total += (product.getPrice()) * state.getCart().getQuantityById().get(product.getId());
    }

    return total;
  }
}
