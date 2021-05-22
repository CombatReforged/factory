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
        wrapped.add(index, ((WrappedNBTValue) value).unwrap());
    }

    @Override
    public void set(int index, NBTValue value) {
        wrapped.set(index, ((WrappedNBTValue) value).unwrap());
    }

    @Override
    public void remove(int index) {
        wrapped.remove(index);
    }

    @Override
    public NBTValue get(int index) {
        return Wrapped.wrap(wrapped.get(index), WrappedNBTValue.class);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @NotNull
    @Override
    public Iterator<NBTValue> iterator() {
        return new NBTListIterator(this);
    }

    private static class NBTListIterator implements Iterator<NBTValue> {
        private final NBTList list;
        private int index;

        public NBTListIterator(NBTList list) {
            this.list = list;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < list.size();
        }

        @Override
        public NBTValue next() {
            NBTValue value = list.get(this.index);
            this.index++;
            return value;
        }
    }
}
