package io.roisagiv.android.redux.todo;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;
import io.roisagiv.android.redux.thunk.AsyncAction;

public enum TodoAction {
  AddTodo,
  DeleteTodo,
  EditTodo,
  CompleteTodo,
  CompletingTodo,
  CompletedTodo,
  CompleteAll,
  ClearCompleted;

  public static class AddTodoAction implements Action<TodoAction> {

    private final String text;

    public AddTodoAction(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

    @Override public TodoAction getType() {
      return TodoAction.AddTodo;
    }
  }

  public static class CompleteTodoAction implements AsyncAction<TodoAction, TodoState> {

    private final int id;

    public CompleteTodoAction(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override public TodoAction getType() {
      return CompleteTodo;
    }

    @Override
    public void call(final Dispatcher<TodoAction> dispatcher, GetState<TodoState> getState) {
      dispatcher.dispatch(new CompletingTodoAction(id));
      new Thread(new Runnable() {
        @Override public void run() {
          // called server
          dispatcher.dispatch(new CompletedTodoAction(id));
        }
      }).start();
    }
  }

  public static class CompletingTodoAction implements Action<TodoAction> {

    private final int id;

    public CompletingTodoAction(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override public TodoAction getType() {
      return CompletingTodo;
    }
  }

  public static class CompletedTodoAction implements Action<TodoAction> {

    private final int id;

    public CompletedTodoAction(int id) {
      this.id = id;
    }

    public int getId() {
      return id;
    }

    @Override public TodoAction getType() {
      return CompletedTodo;
    }
  }
}
