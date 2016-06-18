package io.roisagiv.android.redux.todo;

public class TodoActionCreator {
  public static TodoAction.AddTodoAction addTodo(String text) {
    return new TodoAction.AddTodoAction(text);
  }

  public static TodoAction.CompleteTodoAction completeTodo(int id) {
    return new TodoAction.CompleteTodoAction(id);
  }
}
