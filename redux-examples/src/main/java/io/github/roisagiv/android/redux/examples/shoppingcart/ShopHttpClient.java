package io.github.roisagiv.android.redux.examples.shoppingcart;

import android.support.annotation.VisibleForTesting;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import io.github.roisagiv.android.redux.examples.BuildConfig;

public class ShopHttpClient {
  private final String baseUrl;
  private final AsyncHttpClient httpClient;

  @VisibleForTesting protected static ShopHttpClient testableClient;

  public ShopHttpClient(String baseUrl) {
    this.baseUrl = baseUrl;
    httpClient = new AsyncHttpClient();
  }

  public static ShopHttpClient createDefaultClient() {
    if (testableClient != null) {
      return testableClient;
    } else {
      return new ShopHttpClient(BuildConfig.SHOPPING_CART_BASE_URL);
    }
  }

  public void getAllProducts(JsonHttpResponseHandler handler) {
    httpClient.get(baseUrl, new RequestParams(), handler);
  }
}
