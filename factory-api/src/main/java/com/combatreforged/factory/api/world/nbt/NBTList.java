package com.combatreforged.factory.api.world.nbt;

import com.combatreforged.factory.api.builder.Builder;

import java.util.List;

public interface NBTList extends Iterable<NBTValue>, NBTValue {
    default void add(NBTValue value) {
        add(size(), value);
    }
    default void addAll(List<NBTValue> values) {
        for (NBTValue value : values) {
            add(value);
        }
    }
    void add(int index, NBTValue value);

    void set(int index, NBTValue value);

    void remove(int index);

    NBTValue get(int index);

    int size();

    static NBTList create() {
        return create(null);
    }

    static NBTList create(List<NBTValue> values) {
        return Builder.getInstance().createNBTList(values);
    }
}
