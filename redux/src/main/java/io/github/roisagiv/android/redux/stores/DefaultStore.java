package io.github.roisagiv.android.redux.stores;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import io.github.roisagiv.android.redux.Action;
import io.github.roisagiv.android.redux.Dispatcher;
import io.github.roisagiv.android.redux.Middleware;
import io.github.roisagiv.android.redux.Reducer;
import io.github.roisagiv.android.redux.State;
import io.github.roisagiv.android.redux.Store;

public class DefaultStore<StateType extends State, ActionType extends Enum>
    extends BaseStore<StateType, ActionType>
    implements Store<StateType, ActionType>, Handler.Callback {

  private final Handler handler;
  private final Dispatcher<ActionType> dispatcher;

  @SafeVarargs
  public DefaultStore(Looper looper, Reducer<StateType, ActionType> reducer, StateType defaultState,
      Middleware<StateType, ActionType>... middleware) {
    super(reducer, defaultState, middleware);
    handler = new Handler(looper, this);

    dispatcher = applyMiddleware(middleware, new Dispatcher<ActionType>() {
      @Override public void dispatch(Action<ActionType> action) {
        Message.obtain(handler, 0, action).sendToTarget();
      }
    });
  }

  @Override public void dispatch(Action<ActionType> action) {
    this.dispatcher.dispatch(action);
  }

  @Override public boolean handleMessage(Message msg) {
    Action<ActionType> action = (Action<ActionType>) msg.obj;
    setState(this.getReducer().handleAction(action, getState()));

    return true;
  }
}
