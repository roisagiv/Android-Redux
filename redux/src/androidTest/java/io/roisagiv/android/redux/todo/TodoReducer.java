package io.roisagiv.android.redux.todo;

import io.roisagiv.android.redux.Action;
import io.roisagiv.android.redux.Reducer;
import java.util.List;

public class TodoReducer implements Reducer<TodoState, TodoAction> {

  @Override public TodoState handleAction(Action<TodoAction> action, TodoState state) {
    TodoState newState = new TodoState();
    newState.getTodos().addAll(state.getTodos());

    Todo todo;
    switch (action.getType()) {

      case AddTodo:
        TodoAction.AddTodoAction addTodoAction = (TodoAction.AddTodoAction) action;

        int id = maxId(newState.getTodos());

        newState.getTodos().add(new Todo(id, addTodoAction.getText(), false));
        break;

      case DeleteTodo:
        break;
      case EditTodo:
        break;
      case CompleteTodo:
        TodoAction.CompleteTodoAction completeTodoAction = (TodoAction.CompleteTodoAction) action;
        todo = findById(completeTodoAction.getId(), newState.getTodos());
        todo.setCompleting(true);
        break;

      case CompletedTodo:
        TodoAction.CompletedTodoAction completedTodoAction =
            (TodoAction.CompletedTodoAction) action;
        todo = findById(completedTodoAction.getId(), newState.getTodos());
        todo.setCompleted(true);
        break;
      case CompleteAll:
        break;
      case ClearCompleted:
        break;
    }
    return newState;
  }

  private int maxId(List<Todo> todos) {
    int result = 0;

    if (todos.size() == 0) {
      return result;
    }

    for (Todo todo : todos) {
      result = Math.max(result, todo.getId());
    }

    return result + 1;
  }

  private Todo findById(int id, List<Todo> todos) {
    for (Todo todo : todos) {
      if (todo.getId() == id) {
        return todo;
      }
    }
    // else
    return null;
  }
}
