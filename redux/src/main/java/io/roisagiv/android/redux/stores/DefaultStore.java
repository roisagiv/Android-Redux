package io.roisagiv.android.redux.stores;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Reducer;
import io.roisagiv.android.redux.State;
import io.roisagiv.android.redux.Store;

public class DefaultStore<StateType extends State, ActionType extends Enum>
    extends BaseStore<StateType, ActionType>
    implements Store<StateType, ActionType>, Handler.Callback {

  private final Handler handler;
  private final Dispatcher<ActionType> dispatcher;

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
