package com.combatreforged.factory.api.world.effect;

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

        public boolean equals(Object obj) {
            return obj instanceof Unidentified && obj.getClass() == getClass() && ((Unidentified) obj).getId().equals(this.id);
        }

        @Override
        public Type getType() {
            return type;
        }
    }
}
