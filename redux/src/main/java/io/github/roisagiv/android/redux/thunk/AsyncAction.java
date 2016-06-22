package io.github.roisagiv.android.redux.thunk;

import io.github.roisagiv.android.redux.Action;
import io.github.roisagiv.android.redux.Dispatcher;
import io.github.roisagiv.android.redux.GetState;
import io.github.roisagiv.android.redux.State;

public interface AsyncAction<ActionType extends Enum, StateType extends State>
    extends Action<ActionType> {

  void call(Dispatcher<ActionType> dispatcher, GetState<StateType> getState);
}
