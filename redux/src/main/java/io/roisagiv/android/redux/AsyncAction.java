package io.roisagiv.android.redux;

public interface AsyncAction<ActionType extends Enum, StateType extends State>
    extends Action<ActionType> {

  void call(Dispatcher<ActionType> dispatcher, GetState<StateType> getState);
}
