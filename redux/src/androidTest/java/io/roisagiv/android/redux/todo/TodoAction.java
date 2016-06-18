package io.roisagiv.android.redux.todo;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.AsyncAction;
import io.roisagiv.android.redux.Dispatcher;
import io.roisagiv.android.redux.GetState;

public enum TodoAction {
  AddTodo,
  DeleteTodo,
  EditTodo,
  CompleteTodo,
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
      new Thread(new Runnable() {
        @Override public void run() {
          // called server
          dispatcher.dispatch(new CompletedTodoAction(id));
        }
      }).start();
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
