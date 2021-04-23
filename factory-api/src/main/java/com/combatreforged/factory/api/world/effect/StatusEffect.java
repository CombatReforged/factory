package com.combatreforged.factory.api.world.effect;

import java.util.Objects;

public interface StatusEffect {
    Type getType();

    enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL
    }

    class Unidentified implements StatusEffect {
        private final String id;
        private final Type type;

        public Unidentified(String id, Type type) {
            this.id = id;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Unidentified
                    && ((Unidentified) obj).getId().equals(this.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.id, this.type);
        }

        @Override
        public Type getType() {
            return type;
        }
    }
}
