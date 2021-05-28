package com.combatreforged.factory.api.world.item;

import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.interfaces.StringIdentified;

public interface Enchantment extends Namespaced, ObjectMapped {
    boolean isCurse();

    int getMaxLevel();

    boolean canBeAppliedTo(ItemStack itemStack);

    abstract class Other implements Enchantment, StringIdentified {
    }
}
