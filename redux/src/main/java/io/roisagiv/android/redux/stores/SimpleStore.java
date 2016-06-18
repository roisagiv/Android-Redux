package io.roisagiv.android.redux.stores;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.AsyncAction;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Reducer;
import io.roisagiv.android.redux.State;
import io.roisagiv.android.redux.Store;

public class SimpleStore<StateType extends State, ActionType extends Enum>
    extends BaseStore<StateType, ActionType> implements Store<StateType, ActionType> {

  private final Dispatcher<ActionType> dispatcher;

  public SimpleStore(Reducer<StateType, ActionType> reducer, StateType defaultState,
      Middleware<StateType, ActionType>... middleware) {
    super(reducer, defaultState, middleware);

    dispatcher = applyMiddleware(middleware, new Dispatcher<ActionType>() {
      @Override public void dispatch(Action<ActionType> action) {
        setState(SimpleStore.this.getReducer().handleAction(action, getState()));

        if (action instanceof AsyncAction<?, ?>) {
          AsyncAction<ActionType, StateType> asyncAction =
              (AsyncAction<ActionType, StateType>) action;
          asyncAction.call(this, SimpleStore.this);
        }
      }
    });
  }

  @Override public void dispatch(Action<ActionType> action) {
    dispatcher.dispatch(action);
  }
}
