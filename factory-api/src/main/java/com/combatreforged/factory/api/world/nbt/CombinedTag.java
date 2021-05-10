package com.combatreforged.factory.api.world.nbt;

import java.util.ArrayList;
import java.util.List;

public interface CombinedTag {
    <T> T get(String name, Type<T> type);
    <T> T getOrDefault(String name, Type<T> type, T or);
    <T> void set(String name, T value);
    boolean has(String name);
    default <T> boolean has(String name, Type<T> type) {
        return has(name) && get(name, type) != null;
    }

    String toString();

    class Type<T> {
        public static final Type<CombinedTag> TAG = new Type<>(CombinedTag.class);
        public static final Type<Short> SHORT = new Type<>(Short.class);
        public static final Type<Double> DOUBLE = new Type<>(Double.class);
        public static final Type<Float> FLOAT = new Type<>(Float.class);
        public static final Type<Byte> BYTE = new Type<>(Byte.class);
        public static final Type<Integer> INT = new Type<>(Integer.class);
        public static final Type<Long> LONG = new Type<>(Long.class);

        public static final Type<String> STRING = new Type<>(String.class);

        public static final Type<Long[]> LONG_ARRAY = new Type<>(Long[].class);
        public static final Type<Byte[]> BYTE_ARRAY = new Type<>(Byte[].class);
        public static final Type<Integer[]> INT_ARRAY = new Type<>(Integer[].class);

        public static final Type<List<Object>> LIST = new Type<>(new ArrayList<>());

        private final Class<T> type;

        public Type(Class<T> type) {
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        public Type(T type) {
            this.type = (Class<T>) type.getClass();
        }

        public Class<T> getType() {
            return type;
        }
    }
}
