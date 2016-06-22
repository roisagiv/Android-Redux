package io.github.roisagiv.android.redux;

public interface Store<StateType extends State, ActionType extends Enum>
    extends Dispatcher<ActionType>, GetState<StateType> {

  void subscribe(StoreListener listener);

  void unsubscribe(StoreListener listener);

  interface StoreListener {
    void onNewStoreState();
  }
}
