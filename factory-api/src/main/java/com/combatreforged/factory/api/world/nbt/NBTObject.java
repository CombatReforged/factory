package com.combatreforged.factory.api.world.nbt;

import com.combatreforged.factory.api.builder.Builder;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface NBTObject extends NBTValue {
    void set(String id, NBTValue value);
    default void setAll(Map<String, NBTValue> values) {
        for (String key : values.keySet()) {
            set(key, values.get(key));
        }
    }
    @Nullable NBTValue get(String id);
    boolean has(String id);

    int size();

    Set<String> keys();
    default NBTValue.Type getValueType(String id) {
        NBTValue value;
        if ((value = get(id)) == null) return null;
        return value.getType();
    }

    @Override String toString();

    NBTObject copy();

    static NBTObject create() {
        return create(null);
    }

    static NBTObject create(Map<String, NBTValue> values) {
        return Builder.getInstance().createNBTObject(values);
    }

    static NBTObject of(String nbtString) {
        return Builder.getInstance().createNBTObjectFromString(nbtString);
    }
}
