package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.CompoundTag;

import java.util.Set;

public class WrappedNBTObject extends WrappedNBTValue implements NBTObject, NBTValue {
    private final CompoundTag wrappedCompound;

    public WrappedNBTObject(CompoundTag wrappedCompound) {
        super(wrappedCompound);
        this.wrappedCompound = wrappedCompound;
    }

    @Override
    public CompoundTag unwrap() {
        return wrappedCompound;
    }

    @Override
    public NBTValue get(String id) {
        return new WrappedNBTValue(wrappedCompound.get(id));
    }

    @Override
    public boolean has(String id) {
        return wrappedCompound.contains(id);
    }

    @Override
    public int size() {
        return wrappedCompound.size();
    }

    @Override
    public Set<String> keys() {
        return wrappedCompound.getAllKeys();
    }

    @Override
    public NBTObject copy() {
        return Wrapped.wrap(wrappedCompound.copy(), WrappedNBTObject.class);
    }

    @Override
    public void set(String id, NBTValue value) {
        wrappedCompound.put(id, ((WrappedNBTValue) value).unwrap());
    }

    @Override
    public String asString() {
        return wrappedCompound.getAsString();
    }

    @Override
    public NBTObject asObject() {
        return this;
    }

    @Override
    public Type getType() {
        return Type.OBJECT;
    }
}
