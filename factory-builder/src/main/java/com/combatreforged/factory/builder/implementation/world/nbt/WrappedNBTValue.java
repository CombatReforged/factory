package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.exception.UnassignableTypeException;
import com.combatreforged.factory.api.world.nbt.NBTList;
import com.combatreforged.factory.api.world.nbt.NBTObject;
import com.combatreforged.factory.api.world.nbt.NBTValue;
import com.combatreforged.factory.builder.exception.WrappingException;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.ObjectMappings;
import net.minecraft.nbt.*;

public class WrappedNBTValue extends Wrapped<Tag> implements NBTValue {
    public WrappedNBTValue(Tag wrapped) {
        super(wrapped);
    }

    @Override
    public short asShort() {
        try {
            return ((ShortTag) wrapped).getAsShort();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a short", wrapped.getAsString()));
        }
    }

    @Override
    public double asDouble() {
        try {
            return ((DoubleTag) wrapped).getAsDouble();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a double", wrapped.getAsString()));
        }
    }

    @Override
    public float asFloat() {
        try {
            return ((FloatTag) wrapped).getAsFloat();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a float", wrapped.getAsString()));
        }
    }

    @Override
    public byte asByte() {
        try {
            return ((ByteTag) wrapped).getAsByte();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a byte", wrapped.getAsString()));
        }
    }

    @Override
    public int asInt() {
        try {
            return ((IntTag) wrapped).getAsInt();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not an int", wrapped.getAsString()));
        }
    }

    @Override
    public long asLong() {
        try {
            return ((LongTag) wrapped).getAsLong();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a long", wrapped.getAsString()));
        }
    }

    @Override
    public long[] asLongArray() {
        try {
            return ((LongArrayTag) wrapped).getAsLongArray();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a long[]", wrapped.getAsString()));
        }
    }

    @Override
    public byte[] asByteArray() {
        try {
            return ((ByteArrayTag) wrapped).getAsByteArray();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not a byte[]", wrapped.getAsString()));
        }
    }

    @Override
    public int[] asIntArray() {
        try {
            return ((IntArrayTag) wrapped).getAsIntArray();
        } catch (ClassCastException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not an int[]", wrapped.getAsString()));
        }
    }

    @Override
    public String asString() {
        return wrapped.getAsString();
    }

    @Override
    public NBTObject asObject() {
        try {
            return Wrapped.wrap(wrapped, WrappedNBTObject.class);
        } catch (ClassCastException | WrappingException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not an NBTObject", wrapped.getAsString()));
        }
    }

    @Override
    public NBTList asList() {
        try {
            return Wrapped.wrap(wrapped, WrappedNBTList.class);
        } catch (ClassCastException | WrappingException e) {
            throw new UnassignableTypeException(String.format("\"%s\" not an NBTList", wrapped.getAsString()));
        }
    }

    @Override
    public Type getType() {
        return ObjectMappings.NBT_VALUE_TYPES.inverse().get(wrapped.getClass());
    }
}
