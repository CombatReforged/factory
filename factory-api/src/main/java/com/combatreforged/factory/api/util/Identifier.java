package com.combatreforged.factory.api.util;

public class Identifier {
    private final String namespace;
    private final String id;

    public Identifier(String namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getId() {
        return id;
    }
}
