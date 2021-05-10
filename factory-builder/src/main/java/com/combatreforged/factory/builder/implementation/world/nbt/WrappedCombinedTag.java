package com.combatreforged.factory.builder.implementation.world.nbt;

import com.combatreforged.factory.api.exception.UnassignableTypeException;
import com.combatreforged.factory.api.world.nbt.CombinedTag;
import com.combatreforged.factory.builder.implementation.Wrapped;
import net.minecraft.nbt.*;

public class WrappedCombinedTag extends Wrapped<CompoundTag> implements CombinedTag {
    public WrappedCombinedTag(CompoundTag wrapped) {
        super(wrapped);
    }

    @Override
    public <T> T get(String name, Type<T> type) {
        return null; //TODO
    }

    @Override
    public <T> T getOrDefault(String name, Type<T> type, T or) {
        return null; //TODO
    }

    @Override
    public <T> void set(String name, T value) {
        //TODO
    }

    @SuppressWarnings("unchecked")
    static <T> T convert(Tag tag, Type<T> type) {
        try {
            if (type == Type.TAG)
                return (T) Wrapped.wrap(tag, WrappedCombinedTag.class);
            else if (type == Type.SHORT)
                return (T) Short.valueOf(((ShortTag) tag).getAsShort());
            else if (type == Type.DOUBLE)
                return (T) Double.valueOf(((DoubleTag) tag).getAsDouble());
            else if (type == Type.FLOAT) 
                return (T) Float.valueOf(((FloatTag) tag).getAsFloat());
            else if (type == Type.BYTE) 
                return (T) Byte.valueOf(((ByteTag) tag).getAsByte());
            else if (type == Type.INT) 
                return (T) Integer.valueOf(((IntTag) tag).getAsInt());
            else if (type == Type.LONG) 
                return (T) Long.valueOf(((LongTag) tag).getAsLong());
            else if (type == Type.STRING) 
                return (T) tag.getAsString();
            else if (type == Type.LONG_ARRAY) 
                return (T) ((LongArrayTag) tag).getAsLongArray();
            else if (type == Type.BYTE_ARRAY) 
                return (T) ((ByteArrayTag) tag).getAsByteArray();
            else if (type == Type.INT_ARRAY) 
                return (T) ((IntArrayTag) tag).getAsIntArray();
            else if (type == Type.LIST)
                return null; //TODO List tags
            
            throw new UnassignableTypeException("Types don't match");
        } catch (ClassCastException e) {
            throw new UnassignableTypeException("Types don't match");
        }
    }

    @Override
    public boolean has(String name) {
        return false; //TODO
    }
}
