package com.combatreforged.factory.api.world.nbt;

public interface NBTList extends Iterable<NBTValue> {
    default void add(NBTValue value) {
        add(size(), value);
    }
    void add(int index, NBTValue value);

    void set(int index, NBTValue value);

    void remove(int index);

    NBTValue get(int index);

    int size();
}
