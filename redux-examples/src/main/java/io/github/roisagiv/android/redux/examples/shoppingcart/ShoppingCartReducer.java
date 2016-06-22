package io.github.roisagiv.android.redux.examples.shoppingcart;

import io.github.roisagiv.android.redux.Action;
import io.github.roisagiv.android.redux.Reducer;
import java.util.List;

public class ShoppingCartReducer implements Reducer<ShoppingCartState, ShoppingCartAction> {

  @Override public ShoppingCartState handleAction(Action<ShoppingCartAction> action,
      ShoppingCartState state) {
    ShoppingCartState result = Parcelables.deepCopy(state);

    if (action == null) {
      return result;
    }

    switch (action.getType()) {
      case ReceiveProducts:
        ShoppingCartAction.ReceiveProductsAction receiveProductsAction =
            (ShoppingCartAction.ReceiveProductsAction) action;

        List<ShoppingCartState.Product> products = receiveProductsAction.getProducts();

        result.getProducts().getVisibleIds().clear();
        result.getProducts().getById().clear();

        for (ShoppingCartState.Product product : products) {
          result.getProducts().getById().put(product.getId(), product);
          result.getProducts().getVisibleIds().add(product.getId());
        }

        break;

      case AddToCart:
        ShoppingCartAction.AddToCartAction addToCartAction =
            (ShoppingCartAction.AddToCartAction) action;

        ShoppingCartState.Cart cart = result.getCart();
        int productId = addToCartAction.getProductId();

        if (!cart.getAddedIds().contains(productId)) {
          cart.getAddedIds().add(productId);
        }
        int quantity = 0;
        if (cart.getQuantityById().containsKey(productId)) {
          quantity = cart.getQuantityById().get(productId);
        }

        cart.getQuantityById().put(productId, quantity + 1);
        ShoppingCartState.Product product = result.getProducts().getById().get(productId);
        product.setInventory(product.getInventory() - 1);

        break;
    }

    return result;
  }
}
