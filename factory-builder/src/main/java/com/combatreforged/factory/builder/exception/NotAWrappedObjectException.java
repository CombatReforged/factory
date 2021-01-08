package com.combatreforged.factory.builder.exception;

public class NotAWrappedObjectException extends Exception {
    public NotAWrappedObjectException() {
        super("Not a wrapped object!");
    }
}
