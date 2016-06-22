package io.github.roisagiv.android.redux.examples.shoppingcart;

import android.os.Looper;
import io.github.roisagiv.android.redux.Middleware;
import io.github.roisagiv.android.redux.Store;
import io.github.roisagiv.android.redux.stores.DefaultStore;
import io.github.roisagiv.android.redux.thunk.ThunkMiddleware;
import java.util.ArrayList;
import java.util.List;

public class CreateStore {

  private static final List<Middleware<ShoppingCartState, ShoppingCartAction>> MIDDLEWARE =
      new ArrayList<>();

  public static void addMiddleware(Middleware<ShoppingCartState, ShoppingCartAction> middleware) {
    MIDDLEWARE.add(middleware);
  }

  public static void clearMiddleware() {
    MIDDLEWARE.clear();
  }

  public static Store<ShoppingCartState, ShoppingCartAction> createStore() {

    List<Middleware<ShoppingCartState, ShoppingCartAction>> middleware =
        new ArrayList<>(MIDDLEWARE);
    middleware.add(0, new ThunkMiddleware<ShoppingCartState, ShoppingCartAction>());

    return new DefaultStore<>(Looper.getMainLooper(), new ShoppingCartReducer(),
        new ShoppingCartState(), middleware.toArray(new Middleware[middleware.size()]));
  }
}
