package io.github.roisagiv.android.redux.stores;

import io.github.roisagiv.android.redux.Action;
import io.github.roisagiv.android.redux.Dispatcher;
import io.github.roisagiv.android.redux.Middleware;
import io.github.roisagiv.android.redux.Reducer;
import io.github.roisagiv.android.redux.State;
import io.github.roisagiv.android.redux.Store;

public class SimpleStore<StateType extends State, ActionType extends Enum>
    extends BaseStore<StateType, ActionType> implements Store<StateType, ActionType> {

  private final Dispatcher<ActionType> dispatcher;

  public SimpleStore(Reducer<StateType, ActionType> reducer, StateType defaultState,
      Middleware<StateType, ActionType>... middleware) {
    super(reducer, defaultState, middleware);

    dispatcher = applyMiddleware(middleware, new Dispatcher<ActionType>() {
      @Override public void dispatch(Action<ActionType> action) {
        setState(SimpleStore.this.getReducer().handleAction(action, getState()));
      }
    });
  }

  @Override public void dispatch(Action<ActionType> action) {
    dispatcher.dispatch(action);
  }
}
