package com.combatreforged.factory.api.world.nbt;

public interface NBTTag {
    <T> T get(String name, Class<T> type);
    <T> void set(String name, T value);
    boolean has(String name);

    String toString();
}
