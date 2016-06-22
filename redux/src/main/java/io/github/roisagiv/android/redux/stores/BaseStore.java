package io.github.roisagiv.android.redux.stores;

import io.github.roisagiv.android.redux.Dispatcher;
import io.github.roisagiv.android.redux.Middleware;
import io.github.roisagiv.android.redux.Reducer;
import io.github.roisagiv.android.redux.State;
import io.github.roisagiv.android.redux.Store;
import java.util.WeakHashMap;

public abstract class BaseStore<StateType extends State, ActionType extends Enum>
    implements Store<StateType, ActionType> {

  private final Reducer<StateType, ActionType> reducer;
  private final Middleware<StateType, ActionType>[] middleware;
  private final WeakHashMap<Store.StoreListener, Void> listeners;
  private StateType currentState;

  public BaseStore(Reducer<StateType, ActionType> reducer, StateType defaultState,
      Middleware<StateType, ActionType>... middleware) {
    this.reducer = reducer;
    this.currentState = defaultState;
    this.middleware = middleware;
    listeners = new WeakHashMap<>();
  }

  protected Reducer<StateType, ActionType> getReducer() {
    return reducer;
  }

  @Override public StateType getState() {
    return currentState;
  }

  protected void setState(StateType newState) {
    currentState = newState;
    for (StoreListener listener : listeners.keySet()) {
      listener.onNewStoreState();
    }
  }

  protected Dispatcher<ActionType> applyMiddleware(Middleware<StateType, ActionType>[] middleware,
      Dispatcher<ActionType> dispatcher) {
    Dispatcher<ActionType> result = dispatcher;
    if (middleware != null && middleware.length > 0) {
      for (int i = 0; i < middleware.length; i++) {
        result = middleware[i].middleware(this, result);
      }
    }

    return result;
  }

  @Override public void subscribe(StoreListener listener) {
    listeners.put(listener, null);
  }

  @Override public void unsubscribe(StoreListener listener) {
    listeners.remove(listener);
  }
}
