package com.combatreforged.factory.api.world.entity;

import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.interfaces.StringIdentified;

public interface EntityType extends ObjectMapped, Namespaced {
    abstract class Other implements EntityType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
