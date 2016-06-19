package io.roisagiv.android.redux.thunk;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.State;

public interface AsyncAction<ActionType extends Enum, StateType extends State>
    extends Action<ActionType> {

  void call(Dispatcher<ActionType> dispatcher, GetState<StateType> getState);
}
