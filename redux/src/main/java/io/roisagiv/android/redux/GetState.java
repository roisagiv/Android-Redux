package io.roisagiv.android.redux;

public interface GetState<StateType extends State> {
  StateType getState();
}
