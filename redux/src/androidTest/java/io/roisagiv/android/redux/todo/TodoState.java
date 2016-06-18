package io.roisagiv.android.redux.todo;

import io.roisagiv.android.redux.State;
import java.util.ArrayList;
import java.util.List;

public class TodoState implements State {
  public List<Todo> getTodos() {
    return todos;
  }

  private final List<Todo> todos = new ArrayList<>();
}
