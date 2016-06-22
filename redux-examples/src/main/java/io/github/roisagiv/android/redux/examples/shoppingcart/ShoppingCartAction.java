package io.github.roisagiv.android.redux.examples.shoppingcart;

import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import io.github.roisagiv.android.redux.Action;
import io.github.roisagiv.android.redux.Dispatcher;
import io.github.roisagiv.android.redux.GetState;
import io.github.roisagiv.android.redux.thunk.AsyncAction;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public enum ShoppingCartAction {

  ReceiveProducts,
  GetAllProducts,
  AddToCart;

  public static class AddToCartAction implements Action<ShoppingCartAction> {

    private final int productId;

    public AddToCartAction(int productId) {
      this.productId = productId;
    }

    public int getProductId() {
      return productId;
    }

    @Override public ShoppingCartAction getType() {
      return AddToCart;
    }
  }

  public static class GetAllProductsAction
      implements AsyncAction<ShoppingCartAction, ShoppingCartState> {

    @Override public ShoppingCartAction getType() {
      return GetAllProducts;
    }

    @Override public void call(final Dispatcher<ShoppingCartAction> dispatcher,
        GetState<ShoppingCartState> getState) {
      ShopHttpClient client = ShopHttpClient.createDefaultClient();
      client.getAllProducts(new JsonHttpResponseHandler() {
        @Override public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
          super.onSuccess(statusCode, headers, response);
          try {

            if (response == null) return;

            List<ShoppingCartState.Product> products = Json.fromJson(response);
            dispatcher.dispatch(new ReceiveProductsAction(products));
          } catch (JSONException ignored) {
          }
        }
      });
    }
  }

  public static class ReceiveProductsAction implements Action<ShoppingCartAction> {

    private final List<ShoppingCartState.Product> products;

    public ReceiveProductsAction(List<ShoppingCartState.Product> products) {
      this.products = products;
    }

    public List<ShoppingCartState.Product> getProducts() {
      return products;
    }

    @Override public ShoppingCartAction getType() {
      return ReceiveProducts;
    }
  }
}
