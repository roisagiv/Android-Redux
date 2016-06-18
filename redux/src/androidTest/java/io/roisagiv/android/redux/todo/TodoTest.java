package io.roisagiv.android.redux.todo;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.Middleware;
import io.roisagiv.android.redux.Store;
import io.roisagiv.android.redux.stores.DefaultStore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class) public class TodoTest {

  @Test public void shouldAddTodo() {
    TodoState initialState = new TodoState();
    initialState.getTodos().add(new Todo(0, "whatever", false));

    Store<TodoState, TodoAction> store =
        new DefaultStore<>(InstrumentationRegistry.getTargetContext().getMainLooper(),
            new TodoReducer(), initialState);

    TodoState newState;

    store.dispatch(TodoActionCreator.addTodo("another text"));
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    newState = store.getState();
    assertThat(newState.getTodos()).hasSize(2);
    assertThat(newState.getTodos().get(1).getId()).isEqualTo(1);
    assertThat(newState.getTodos().get(1).getText()).isEqualTo("another text");
    assertThat(newState.getTodos().get(1).isCompleted()).isFalse();

    store.dispatch(TodoActionCreator.addTodo("yet another text"));
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    newState = store.getState();
    assertThat(newState.getTodos()).hasSize(3);
    assertThat(newState.getTodos().get(2).getId()).isEqualTo(2);
    assertThat(newState.getTodos().get(2).getText()).isEqualTo("yet another text");
    assertThat(newState.getTodos().get(2).isCompleted()).isFalse();
  }

  @Test public void shouldCallMiddleware() {
    final int[] middlewareInvoked = { 0, 0 };

    Middleware<TodoState, TodoAction> middleware1 = new Middleware<TodoState, TodoAction>() {
      @Override public Dispatcher<TodoAction> middleware(GetState<TodoState> getState,
          final Dispatcher<TodoAction> dispatcher) {
        return new Dispatcher<TodoAction>() {
          @Override public void dispatch(Action<TodoAction> action) {
            middlewareInvoked[0]++;
            dispatcher.dispatch(action);
          }
        };
      }
    };

    Middleware<TodoState, TodoAction> middleware2 = new Middleware<TodoState, TodoAction>() {
      @Override public Dispatcher<TodoAction> middleware(GetState<TodoState> getState,
          final Dispatcher<TodoAction> dispatcher) {
        return new Dispatcher<TodoAction>() {
          @Override public void dispatch(Action<TodoAction> action) {
            middlewareInvoked[1]++;
            dispatcher.dispatch(action);
          }
        };
      }
    };

    Store<TodoState, TodoAction> store =
        new DefaultStore<>(InstrumentationRegistry.getTargetContext().getMainLooper(),
            new TodoReducer(), new TodoState(), middleware1, middleware2);

    store.dispatch(TodoActionCreator.addTodo("123"));
    store.dispatch(TodoActionCreator.addTodo("456"));
    store.dispatch(TodoActionCreator.addTodo("789"));

    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    assertThat(middlewareInvoked[0]).isEqualTo(3);
    assertThat(middlewareInvoked[1]).isEqualTo(3);
  }

  @Test public void shouldCompleteTodo() throws InterruptedException {
    TodoState initialState = new TodoState();
    initialState.getTodos().add(new Todo(0, "whatever", false));
    initialState.getTodos().add(new Todo(1, "another", false));

    Store<TodoState, TodoAction> store =
        new DefaultStore<>(InstrumentationRegistry.getTargetContext().getMainLooper(),
            new TodoReducer(), initialState);

    TodoState newState;

    store.dispatch(TodoActionCreator.completeTodo(1));
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    newState = store.getState();
    assertThat(newState.getTodos().get(1).isCompleting()).isTrue();
    assertThat(newState.getTodos().get(1).isCompleted()).isFalse();

    Thread.sleep(100);
    InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    newState = store.getState();
    assertThat(newState.getTodos().get(1).isCompleting()).isTrue();
    assertThat(newState.getTodos().get(1).isCompleted()).isTrue();
  }
}
