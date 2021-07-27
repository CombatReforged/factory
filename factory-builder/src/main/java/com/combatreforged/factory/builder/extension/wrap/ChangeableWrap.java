package com.combatreforged.factory.builder.extension.wrap;

public interface ChangeableWrap<T> extends Wrap<T> {
    void setWrap(T wrap);
}
