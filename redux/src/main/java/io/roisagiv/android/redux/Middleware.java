package io.roisagiv.android.redux;

public interface Middleware<StateType extends State, ActionType extends Enum> {

  Dispatcher<ActionType> middleware(GetState<StateType> getState, Dispatcher<ActionType> dispatcher);
}
