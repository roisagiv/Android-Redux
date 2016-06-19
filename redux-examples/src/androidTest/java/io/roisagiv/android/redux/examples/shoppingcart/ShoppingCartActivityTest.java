package io.roisagiv.android.redux.examples.shoppingcart;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.loopj.android.http.JsonHttpResponseHandler;
import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Store;
import io.roisagiv.android.redux.examples.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class) public class ShoppingCartActivityTest {

  @Rule public final IntentsTestRule<ShoppingCartActivity> activityRule =
      new IntentsTestRule<>(ShoppingCartActivity.class, true, false);

  private TestableShopHttpClient testableShopHttpClient;

  @Before public void beforeEach() {
    CreateStore.clearMiddleware();
    testableShopHttpClient = new TestableShopHttpClient(null);
    ShopHttpClient.testableClient = testableShopHttpClient;
  }

  @Test public void shouldCallActionOnProductItemClick() {
    // arrange
    ActionLogMiddleware middleware = new ActionLogMiddleware();
    CreateStore.addMiddleware(middleware);

    activityRule.launchActivity(
        new Intent(InstrumentationRegistry.getTargetContext(), ShoppingCartActivity.class));

    List<ShoppingCartState.Product> products =
        Arrays.asList(new ShoppingCartState.Product(3, "Product 3", 3.99, 3),
            new ShoppingCartState.Product(4, "Product 4", 4.99, 4));

    activityRule.getActivity().store.dispatch(
        new ShoppingCartAction.ReceiveProductsAction(products));
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    // act
    onData(anything()).inAdapterView(withId(R.id.listview_products)).atPosition(1).perform(click());

    // assert
    assertThat(middleware.actionsCalled).hasSize(3);
    ShoppingCartAction.AddToCartAction action =
        (ShoppingCartAction.AddToCartAction) middleware.actionsCalled.get(2);
    assertThat(action.getType()).isEqualTo(ShoppingCartAction.AddToCart);
    assertThat(action.getProductId()).isEqualTo(4);
  }

  @Test public void shouldGetAllProductsOnCreate() {
    // arrange
    ActionLogMiddleware middleware = new ActionLogMiddleware();
    CreateStore.addMiddleware(middleware);

    // act
    activityRule.launchActivity(
        new Intent(InstrumentationRegistry.getTargetContext(), ShoppingCartActivity.class));

    // assert
    assertThat(middleware.actionsCalled).hasSize(1);
    assertThat(middleware.actionsCalled.get(0)).isInstanceOf(
        ShoppingCartAction.GetAllProductsAction.class);
  }

  @Test public void shouldRenderCartItemsAndTotal() throws JSONException {
    // arrange

    List<ShoppingCartState.Product> products =
        Arrays.asList(new ShoppingCartState.Product(1, "Product 1", 1.99, 1),
            new ShoppingCartState.Product(2, "Product 2", 2.99, 2),
            new ShoppingCartState.Product(3, "Product 3", 3.99, 3),
            new ShoppingCartState.Product(4, "Product 4", 4.99, 4));

    testableShopHttpClient.getAllProductsResponse = Json.toJson(products);

    activityRule.launchActivity(
        new Intent(InstrumentationRegistry.getTargetContext(), ShoppingCartActivity.class));
    Store<ShoppingCartState, ShoppingCartAction> store = activityRule.getActivity().store;

    // act
    store.dispatch(new ShoppingCartAction.AddToCartAction(3));
    store.dispatch(new ShoppingCartAction.AddToCartAction(3));
    store.dispatch(new ShoppingCartAction.AddToCartAction(4));
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    // assert
    onData(anything()).inAdapterView(withId(R.id.listview_cart))
        .atPosition(0)
        .check(matches(withText(String.format("%s - $%s x %s", "Product 3", 3.99, 2))));
    onData(anything()).inAdapterView(withId(R.id.listview_cart))
        .atPosition(1)
        .check(matches(withText(String.format("%s - $%s x %s", "Product 4", 4.99, 1))));

    onView(withId(R.id.textview_total)).check(
        matches(withText(String.format("Total: $%s", 12.97))));
  }

  @Test public void shouldRenderListOfProducts() throws JSONException {
    // arrange
    List<ShoppingCartState.Product> products =
        Arrays.asList(new ShoppingCartState.Product(1, "Product 1", 1.99, 1),
            new ShoppingCartState.Product(2, "Product 2", 2.99, 2),
            new ShoppingCartState.Product(3, "Product 3", 3.99, 3),
            new ShoppingCartState.Product(4, "Product 4", 4.99, 4));

    testableShopHttpClient.getAllProductsResponse = Json.toJson(products);

    // act
    activityRule.launchActivity(
        new Intent(InstrumentationRegistry.getTargetContext(), ShoppingCartActivity.class));

    InstrumentationRegistry.getInstrumentation().waitForIdleSync();

    // assert
    for (int i = 0; i < products.size(); i++) {
      ShoppingCartState.Product product = products.get(i);

      onData(anything()).inAdapterView(withId(R.id.listview_products))
          .atPosition(i)
          .check(
              matches(withText(String.format("%s - $%s", product.getTitle(), product.getPrice()))));
    }
  }

  private static class ActionLogMiddleware
      implements Middleware<ShoppingCartState, ShoppingCartAction> {

    private final List<Action<ShoppingCartAction>> actionsCalled;

    private ActionLogMiddleware() {
      actionsCalled = new ArrayList<>();
    }

    @Override public Dispatcher<ShoppingCartAction> middleware(GetState<ShoppingCartState> getState,
        final Dispatcher<ShoppingCartAction> dispatcher) {
      return new Dispatcher<ShoppingCartAction>() {
        @Override public void dispatch(Action<ShoppingCartAction> action) {
          actionsCalled.add(action);
          dispatcher.dispatch(action);
        }
      };
    }
  }

  private static class TestableShopHttpClient extends ShopHttpClient {
    private JSONArray getAllProductsResponse;

    public TestableShopHttpClient(String baseUrl) {
      super(baseUrl);
    }

    @Override public void getAllProducts(JsonHttpResponseHandler handler) {
      handler.onSuccess(200, null, getAllProductsResponse);
    }
  }
}
