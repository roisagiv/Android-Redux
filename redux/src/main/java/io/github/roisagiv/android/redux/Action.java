package io.github.roisagiv.android.redux;

public interface Action<ActionType extends Enum> {
  ActionType getType();
}
