package com.combatreforged.factory.api.world.nbt;

public interface NBTValue {
    short asShort();
    double asDouble();
    float asFloat();
    byte asByte();
    int asInt();
    long asLong();

    long[] asLongArray();
    byte[] asByteArray();
    int[] asIntArray();

    String asString();

    NBTObject asObject();

    NBTList asList();

    Type getType();

    enum Type {
        //TODO
    }

    //TODO implement creation
}
