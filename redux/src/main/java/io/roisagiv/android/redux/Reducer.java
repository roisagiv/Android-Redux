package io.roisagiv.android.redux;

public interface Reducer<StateType extends State, ActionType extends Enum> {
  StateType handleAction(Action<ActionType> action, StateType state);
}
