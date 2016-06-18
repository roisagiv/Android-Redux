package io.roisagiv.android.redux;

public interface Dispatcher<ActionType extends Enum> {

  void dispatch(Action<ActionType> action);
}
