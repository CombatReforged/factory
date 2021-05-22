package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class WrappedNBTList extends Wrapped<ListTag> implements NBTList {
    public WrappedNBTList(ListTag wrapped) {
        super(wrapped);
    }

    @Override
    public void add(int index, NBTValue value) {
        //TODO
    }

    @Override
    public void set(int index, NBTValue value) {
        //TODO
    }

    @Override
    public void remove(int index) {
        //TODO
    }

    @Override
    public NBTValue get(int index) {
        return null; //TODO
    }

    @Override
    public int size() {
        return 0; //TODO
    }

    @NotNull
    @Override
    public Iterator<NBTValue> iterator() {
        return null; //TODO
    }
}
