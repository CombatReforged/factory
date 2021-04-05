package com.combatreforged.factory.api.exception;

public class FactoryPluginException extends RuntimeException {
    public FactoryPluginException(String message) {
        super(message);
    }

    public FactoryPluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
