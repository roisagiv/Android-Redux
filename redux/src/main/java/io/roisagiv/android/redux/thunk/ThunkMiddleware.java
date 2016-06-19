package io.roisagiv.android.redux.thunk;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.State;

public class ThunkMiddleware<StateType extends State, ActionType extends Enum>
    implements Middleware<StateType, ActionType> {

  @Override public Dispatcher<ActionType> middleware(final GetState<StateType> getState,
      final Dispatcher<ActionType> dispatcher) {
    return new Dispatcher<ActionType>() {
      @Override public void dispatch(Action<ActionType> action) {
        if (action instanceof AsyncAction) {
          AsyncAction<ActionType, StateType> asyncAction =
              (AsyncAction<ActionType, StateType>) action;

          asyncAction.call(dispatcher, getState);
        } else {
          dispatcher.dispatch(action);
        }
      }
    };
  }
}
