package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.CompoundTag;

public class WrappedNBTObject extends Wrapped<CompoundTag> implements NBTObject {
    public WrappedNBTObject(CompoundTag wrapped) {
        super(wrapped);
    }

    @Override
    public NBTValue get(String id) {
        return new WrappedNBTValue(wrapped.get(id));
    }

    @Override
    public void set(String id, NBTValue value) {
        wrapped.put(id, ((WrappedNBTValue) value).unwrap());
    }
}
