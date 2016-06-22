package io.github.roisagiv.android.redux.examples.shoppingcart;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import io.github.roisagiv.android.redux.Store;
import io.github.roisagiv.android.redux.examples.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartActivity extends AppCompatActivity
    implements Store.StoreListener, ListView.OnItemClickListener {

  @VisibleForTesting protected Store<ShoppingCartState, ShoppingCartAction> store;
  private ProductListAdapter productListAdapter;
  private CartListAdapter cartListAdapter;
  private TextView cartTotalTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shopping_cart);

    store = CreateStore.createStore();

    productListAdapter = new ProductListAdapter(this);
    ListView productsListView = (ListView) findViewById(R.id.listview_products);
    productsListView.setAdapter(productListAdapter);
    productsListView.setOnItemClickListener(this);

    cartListAdapter = new CartListAdapter(this);
    ListView cartListView = (ListView) findViewById(R.id.listview_cart);
    cartListView.setAdapter(cartListAdapter);

    cartTotalTextView = (TextView) findViewById(R.id.textview_total);

    store.dispatch(new ShoppingCartAction.GetAllProductsAction());
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    ShoppingCartState state = store.getState();
    Integer productId = state.getProducts().getVisibleIds().get(position);
    store.dispatch(new ShoppingCartAction.AddToCartAction(productId));
  }

  @Override public void onNewStoreState() {
    ShoppingCartState state = store.getState();

    productListAdapter.clear();
    productListAdapter.addAll(state.getProducts().getById().values());

    cartListAdapter.clear();
    cartListAdapter.addAll(ShoppingCartSelectors.getProductsInCart(state));
    cartListAdapter.addQuantities(state.getCart().getQuantityById());

    cartTotalTextView.setText(
        String.format("Total: $%s", ShoppingCartSelectors.getCartTotal(state)));
  }

  @Override protected void onPause() {
    store.unsubscribe(this);
    super.onPause();
  }

  @Override protected void onResume() {
    super.onResume();
    store.subscribe(this);
  }

  private static class ProductListAdapter extends ArrayAdapter<ShoppingCartState.Product> {

    public ProductListAdapter(Context context) {
      super(context, android.R.layout.simple_list_item_1,
          new ArrayList<ShoppingCartState.Product>());
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      TextView textView = (TextView) super.getView(position, convertView, parent);
      ShoppingCartState.Product product = getItem(position);
      textView.setText(String.format("%s - $%s", product.getTitle(), product.getPrice()));
      return textView;
    }
  }

  private static class CartListAdapter extends ArrayAdapter<ShoppingCartState.Product> {

    private final Map<Integer, Integer> quantityById;

    public CartListAdapter(Context context) {
      super(context, android.R.layout.simple_list_item_1,
          new ArrayList<ShoppingCartState.Product>());
      this.quantityById = new HashMap<>();
    }

    public void addQuantities(Map<Integer, Integer> quantities) {
      quantityById.clear();
      quantityById.putAll(quantities);
      notifyDataSetChanged();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      TextView textView = (TextView) super.getView(position, convertView, parent);
      ShoppingCartState.Product product = getItem(position);
      textView.setText(String.format("%s - $%s x %s", product.getTitle(), product.getPrice(),
          quantityById.get(product.getId())));
      return textView;
    }
  }
}
