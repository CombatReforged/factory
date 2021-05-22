package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Set;

public class WrappedNBTObject extends Wrapped<CompoundTag> implements NBTObject {
    public WrappedNBTObject(CompoundTag wrapped) {
        super(wrapped);
    }

    @Override
    public NBTValue get(String id) {
        return new WrappedNBTValue(wrapped.get(id));
    }

    @Override
    public boolean has(String id) {
        return wrapped.contains(id);
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public Set<String> keys() {
        return wrapped.getAllKeys();
    }

    @Override
    public NBTObject copy() {
        return Wrapped.wrap(wrapped.copy(), WrappedNBTObject.class);
    }

    @Override
    public void set(String id, NBTValue value) {
        wrapped.put(id, ((WrappedNBTValue) value).unwrap());
    }
}
