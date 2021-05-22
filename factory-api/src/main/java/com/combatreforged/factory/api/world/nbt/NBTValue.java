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
        SHORT, DOUBLE, FLOAT, BYTE, INT, LONG, LONG_ARRAY, BYTE_ARRAY, INT_ARRAY, STRING, OBJECT, LIST
    }

    //TODO implement creation
}
