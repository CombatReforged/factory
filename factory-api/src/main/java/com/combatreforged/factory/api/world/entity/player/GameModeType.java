package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.interfaces.StringIdentified;

public interface GameModeType extends ObjectMapped {
    abstract class Other implements GameModeType, StringIdentified {
        @Override
        public String toString() {
            return this.getId();
        }
    }
}
