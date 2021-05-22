package com.combatreforged.factory.api.world.nbt;

public interface NBTObject {
    void set(String id, NBTValue value);
    NBTValue get(String id); //TODO more methods
}
