package io.github.roisagiv.android.redux.examples.shoppingcart;

import android.support.test.runner.AndroidJUnit4;
import java.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class) public class ShoppingCartReducerTest {

  @Test public void shouldProvideTheInitialState() {
    ShoppingCartReducer reducer = new ShoppingCartReducer();
    ShoppingCartState initialState = new ShoppingCartState();

    ShoppingCartState actual = reducer.handleAction(null, initialState);

    // cannot be the same instance
    assertThat(actual).isNotEqualTo(initialState);

    assertThat(actual).isEqualToComparingFieldByField(initialState);
  }

  @Test public void shouldHandleAddToCartAction() {
    // when cart is empty
    ShoppingCartReducer reducer = new ShoppingCartReducer();
    ShoppingCartState initialState = new ShoppingCartState();
    initialState.getProducts()
        .getById()
        .put(1, new ShoppingCartState.Product(1, "Product 1", 2.99, 1));

    initialState.getProducts()
        .getById()
        .put(2, new ShoppingCartState.Product(2, "Product 2", 3.99, 2));
    ShoppingCartState newState;

    newState = reducer.handleAction(new ShoppingCartAction.AddToCartAction(1), initialState);
    assertThat(newState.getCart().getAddedIds()).contains(1);
    assertThat(newState.getCart().getQuantityById()).containsEntry(1, 1);
    assertThat(newState.getProducts().getById().get(1).getInventory()).isEqualTo(0);

    // when product is already in the cart
    initialState.getCart().getAddedIds().add(1);
    initialState.getCart().getAddedIds().add(2);
    initialState.getCart().getQuantityById().put(1, 1);
    initialState.getCart().getQuantityById().put(2, 1);

    newState = reducer.handleAction(new ShoppingCartAction.AddToCartAction(2), initialState);
    assertThat(newState.getCart().getAddedIds()).contains(1, 2);
    assertThat(newState.getCart().getQuantityById()).containsEntry(2, 2);
    assertThat(newState.getCart().getQuantityById()).containsEntry(1, 1);
  }

  @Test public void shouldHandleReceiveProducts() {
    // when state is empty
    ShoppingCartReducer reducer = new ShoppingCartReducer();
    ShoppingCartState initialState = new ShoppingCartState();

    ShoppingCartState.Product product1 = new ShoppingCartState.Product();
    product1.setId(1);
    product1.setTitle("Product 1");
    ShoppingCartState.Product product2 = new ShoppingCartState.Product();
    product2.setId(2);
    product2.setTitle("Product 2");

    ShoppingCartState newState;
    newState = reducer.handleAction(
        new ShoppingCartAction.ReceiveProductsAction(Arrays.asList(product1, product2)),
        initialState);

    assertThat(newState.getProducts().getById()).containsEntry(1, product1);
    assertThat(newState.getProducts().getById()).containsEntry(2, product2);
    assertThat(newState.getProducts().getVisibleIds()).contains(1, 2);

    // when state is not empty

    ShoppingCartState.Product product3 = new ShoppingCartState.Product();
    product3.setId(3);
    product3.setTitle("Product 3");
    ShoppingCartState.Product product4 = new ShoppingCartState.Product();
    product4.setId(4);
    product4.setTitle("Product 4");

    initialState = newState;
    newState = reducer.handleAction(
        new ShoppingCartAction.ReceiveProductsAction(Arrays.asList(product3, product4)),
        initialState);

    assertThat(newState.getProducts().getById()).containsEntry(3, product3);
    assertThat(newState.getProducts().getById()).containsEntry(4, product4);
    assertThat(newState.getProducts().getVisibleIds()).contains(3, 4);
  }
}
