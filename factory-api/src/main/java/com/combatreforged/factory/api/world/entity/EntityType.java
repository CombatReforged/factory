package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.interfaces.StringIdentified;

public interface EntityType {
    abstract class Other implements EntityType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
