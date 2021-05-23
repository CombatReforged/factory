package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.exception.UnassignableTypeException;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class WrappedNBTList extends WrappedNBTValue implements NBTList {
    private final ListTag wrappedList;

    public WrappedNBTList(ListTag wrappedList) {
        super(wrappedList);
        this.wrappedList = wrappedList;
    }

    @Override
    public ListTag unwrap() {
        return wrappedList;
    }

    @Override
    public void add(int index, NBTValue value) {
        wrappedList.add(index, ((WrappedNBTValue) value).unwrap());
    }

    @Override
    public void set(int index, NBTValue value) {
        wrappedList.set(index, ((WrappedNBTValue) value).unwrap());
    }

    @Override
    public void remove(int index) {
        wrappedList.remove(index);
    }

    @Override
    public NBTValue get(int index) {
        return Wrapped.wrap(wrappedList.get(index), WrappedNBTValue.class);
    }

    @Override
    public int size() {
        return wrappedList.size();
    }

    @NotNull
    @Override
    public Iterator<NBTValue> iterator() {
        return new NBTListIterator(this);
    }

    @Override
    public short asShort() {
        throw new UnassignableTypeException("Cannot return asShort from NBTList");
    }

    @Override
    public double asDouble() {
        throw new UnassignableTypeException("Cannot return asDouble from NBTList");
    }

    @Override
    public float asFloat() {
        throw new UnassignableTypeException("Cannot return asFloat from NBTList");
    }

    @Override
    public byte asByte() {
        throw new UnassignableTypeException("Cannot return asByte from NBTList");
    }

    @Override
    public int asInt() {
        throw new UnassignableTypeException("Cannot return asInt from NBTList");
    }

    @Override
    public long asLong() {
        throw new UnassignableTypeException("Cannot return asLong from NBTList");
    }

    @Override
    public long[] asLongArray() {
        throw new UnassignableTypeException("Cannot return asLongArray from NBTList");
    }

    @Override
    public byte[] asByteArray() {
        throw new UnassignableTypeException("Cannot return asByteArray from NBTList");
    }

    @Override
    public int[] asIntArray() {
        throw new UnassignableTypeException("Cannot return asIntArray from NBTList");
    }

    @Override
    public String asString() {
        return wrappedList.getAsString();
    }

    @Override
    public NBTObject asObject() {
        throw new UnassignableTypeException("Cannot return asObject from NBTList");
    }

    @Override
    public NBTList asList() {
        return this;
    }

    @Override
    public Type getType() {
        return Type.LIST;
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
