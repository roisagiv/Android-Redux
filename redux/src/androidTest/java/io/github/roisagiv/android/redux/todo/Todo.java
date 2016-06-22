package io.github.roisagiv.android.redux.todo;

public class Todo {
  private int id;
  private String text;
  private boolean completed;
  private boolean completing;

  public Todo(int id, String text, boolean completed) {
    this.id = id;
    this.text = text;
    this.completed = completed;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isCompleting() {
    return completing;
  }

  public void setCompleting(boolean completing) {
    this.completing = completing;
  }
}
