package com.combatreforged.factory.api.exception;

public class TaskException extends RuntimeException {
    public TaskException() {
        super("An error occured while running Task: ");
    }
}
