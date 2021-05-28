package com.combatreforged.factory.api.world.nbt;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.interfaces.ObjectMapped;

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

    enum Type implements ObjectMapped {
        SHORT, DOUBLE, FLOAT, BYTE, INT, LONG, LONG_ARRAY, BYTE_ARRAY, INT_ARRAY, STRING, OBJECT, LIST
    }

    static Builder builder() {
        return Builder.getInstance();
    }

    static NBTValue of(short s) {
        return builder().createNBTValue(s);
    }

    static NBTValue of(double d) {
        return builder().createNBTValue(d);
    }

    static NBTValue of(float f) {
        return builder().createNBTValue(f);
    }

    static NBTValue of(byte b) {
        return builder().createNBTValue(b);
    }

    static NBTValue of(int i) {
        return builder().createNBTValue(i);
    }

    static NBTValue of(long l) {
        return builder().createNBTValue(l);
    }

    static NBTValue of(long[] arr) {
        return builder().createNBTValue(arr);
    }

    static NBTValue of(byte[] arr) {
        return builder().createNBTValue(arr);
    }

    static NBTValue of(int[] arr) {
        return builder().createNBTValue(arr);
    }

    static NBTValue of(String string) {
        return builder().createNBTValue(string);
    }
}
