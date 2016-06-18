package io.roisagiv.android.redux.counter;

import android.support.test.runner.AndroidJUnit4;
import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Reducer;
import io.roisagiv.android.redux.State;
import io.roisagiv.android.redux.Store;
import io.roisagiv.android.redux.stores.SimpleStore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class) public class CounterTest {

  @Test public void shouldCallMiddleware() {
    final int[] middlewareInvoked = { 0, 0 };

    Middleware<CounterState, CounterAction> middleware1 =
        new Middleware<CounterState, CounterAction>() {
          @Override public Dispatcher<CounterAction> middleware(GetState<CounterState> getState,
              final Dispatcher<CounterAction> dispatcher) {
            return new Dispatcher<CounterAction>() {
              @Override public void dispatch(Action<CounterAction> action) {
                middlewareInvoked[0]++;
                dispatcher.dispatch(action);
              }
            };
          }
        };

    Middleware<CounterState, CounterAction> middleware2 =
        new Middleware<CounterState, CounterAction>() {
          @Override public Dispatcher<CounterAction> middleware(GetState<CounterState> getState,
              final Dispatcher<CounterAction> dispatcher) {
            return new Dispatcher<CounterAction>() {
              @Override public void dispatch(Action<CounterAction> action) {
                middlewareInvoked[1]++;
                dispatcher.dispatch(action);
              }
            };
          }
        };

    CounterState state = new CounterState();
    state.setCounter(0);

    Store<CounterState, CounterAction> store =
        new SimpleStore<>(new CounterReducer(), state, middleware1, middleware2);

    store.dispatch(CounterActionCreator.increment());
    store.dispatch(CounterActionCreator.increment());
    store.dispatch(CounterActionCreator.increment());

    assertThat(middlewareInvoked[0]).isEqualTo(3);
    assertThat(middlewareInvoked[1]).isEqualTo(3);
  }

  @Test public void shouldCallStoreListenerWhenSubscribe() {
    final Boolean[] listenerCalled = { false };
    CounterState state = new CounterState();
    state.setCounter(0);

    Store<CounterState, CounterAction> store = new SimpleStore<>(new CounterReducer(), state);

    store.subscribe(new Store.StoreListener() {
      @Override public void onNewStoreState() {
        listenerCalled[0] = true;
      }
    });

    store.dispatch(CounterActionCreator.increment());

    assertThat(listenerCalled[0]).isTrue();
  }

  @Test public void shouldDecrementCounter() {
    CounterState state = new CounterState();
    state.setCounter(5);

    Store<CounterState, CounterAction> store = new SimpleStore<>(new CounterReducer(), state);

    CounterState newState;

    store.dispatch(CounterActionCreator.decrement());
    newState = store.getState();
    assertThat(newState.getCounter()).isEqualTo(4);

    store.dispatch(CounterActionCreator.decrement());
    newState = store.getState();
    assertThat(newState.getCounter()).isEqualTo(3);
  }

  @Test public void shouldIncrementCounter() {
    CounterState state = new CounterState();
    state.setCounter(0);

    Store<CounterState, CounterAction> store = new SimpleStore<>(new CounterReducer(), state);

    CounterState newState;

    store.dispatch(CounterActionCreator.increment());
    newState = store.getState();
    assertThat(newState.getCounter()).isEqualTo(1);

    store.dispatch(CounterActionCreator.increment());
    newState = store.getState();
    assertThat(newState.getCounter()).isEqualTo(2);
  }

  @Test public void shouldNotCallListenerWhenUnsubscribe() {
    final Boolean[] listenerCalled = { false };
    CounterState state = new CounterState();
    state.setCounter(0);

    Store<CounterState, CounterAction> store = new SimpleStore<>(new CounterReducer(), state);

    Store.StoreListener listener = new Store.StoreListener() {
      @Override public void onNewStoreState() {
        listenerCalled[0] = true;
      }
    };
    store.subscribe(listener);
    store.unsubscribe(listener);

    store.dispatch(CounterActionCreator.increment());

    assertThat(listenerCalled[0]).isFalse();
  }

  public enum CounterAction {
    Increment,
    Decrement
  }

  public static class CounterState implements State {
    private int counter;

    public int getCounter() {
      return counter;
    }

    public void setCounter(int counter) {
      this.counter = counter;
    }
  }

  public static class CounterReducer implements Reducer<CounterState, CounterAction> {

    @Override public CounterState handleAction(Action<CounterAction> action, CounterState state) {
      CounterState newState = new CounterState();
      newState.setCounter(state.getCounter());
      switch (action.getType()) {

        case Increment:
          newState.setCounter(newState.getCounter() + 1);
          break;
        case Decrement:
          newState.setCounter(newState.getCounter() - 1);
          break;
      }
      return newState;
    }
  }

  public static class CounterActionCreator {

    public static Action<CounterAction> increment() {
      return new Action<CounterAction>() {
        @Override public CounterAction getType() {
          return CounterAction.Increment;
        }
      };
    }

    public static Action<CounterAction> decrement() {
      return new Action<CounterAction>() {
        @Override public CounterAction getType() {
          return CounterAction.Decrement;
        }
      };
    }
  }
}
