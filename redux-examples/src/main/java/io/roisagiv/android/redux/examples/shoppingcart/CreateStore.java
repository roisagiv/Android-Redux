package io.roisagiv.android.redux.examples.shoppingcart;

import android.os.Looper;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Store;
import io.roisagiv.android.redux.stores.DefaultStore;
import io.roisagiv.android.redux.thunk.ThunkMiddleware;
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

    List<Middleware<ShoppingCartState, ShoppingCartAction>> middlewares =
        new ArrayList<>(MIDDLEWARE);
    middlewares.add(0, new ThunkMiddleware<ShoppingCartState, ShoppingCartAction>());

    return new DefaultStore<>(Looper.getMainLooper(), new ShoppingCartReducer(),
        new ShoppingCartState(), middlewares.toArray(new Middleware[middlewares.size()]));
  }
}
