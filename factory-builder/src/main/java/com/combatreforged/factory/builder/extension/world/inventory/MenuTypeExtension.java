package com.combatreforged.factory.builder.extension.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface MenuTypeExtension {
    AbstractContainerMenu create(int i, Inventory inventory);
}
