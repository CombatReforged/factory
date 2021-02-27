package com.combatreforged.factory.builder.exception;

public class NotAWrappedObjectException extends RuntimeException {
    public NotAWrappedObjectException() {
        super("Not a wrapped object!");
    }
}
