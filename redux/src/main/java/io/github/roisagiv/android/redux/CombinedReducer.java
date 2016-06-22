package io.github.roisagiv.android.redux;

import java.util.List;

public class CombinedReducer<StateType extends State, ActionType extends Enum>
    implements Reducer<StateType, ActionType> {

  private final List<Reducer<StateType, ActionType>> reducers;

  public CombinedReducer(List<Reducer<StateType, ActionType>> reducers) {
    this.reducers = reducers;
  }

  @Override public StateType handleAction(Action<ActionType> action, StateType state) {
    StateType nextState = state;

    for (Reducer<StateType, ActionType> reducer : reducers) {
      nextState = reducer.handleAction(action, nextState);
    }

    return nextState;
  }
}
