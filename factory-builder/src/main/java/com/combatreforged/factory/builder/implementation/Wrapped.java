package com.combatreforged.factory.builder.implementation;

import com.combatreforged.factory.builder.exception.NotAWrappedObjectException;
import com.combatreforged.factory.builder.extension.wrap.Wrap;

public abstract class Wrapped<T> {
    protected T wrapped;

    public Wrapped(T wrapped) {
        this.wrapped = wrapped;
    }

    public T unwrap() {
        return wrapped;
    }

    @SuppressWarnings("unchecked")
    public static <U, T extends Wrapped<U>> T wrap(Object unwrapped, Class<T> clazz) {
        if (unwrapped == null) return null;
        try {
            return (T) ((Wrap<U>) unwrapped).wrap();
        } catch (ClassCastException e) {
            throw new NotAWrappedObjectException();
        }
    }
}
