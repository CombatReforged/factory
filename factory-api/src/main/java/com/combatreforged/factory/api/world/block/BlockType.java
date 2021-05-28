package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.interfaces.StringIdentified;
import com.google.errorprone.annotations.Immutable;

@Immutable
public interface BlockType extends ObjectMapped, Namespaced {
    @Immutable
    abstract class Other implements BlockType, StringIdentified {
    }
}
