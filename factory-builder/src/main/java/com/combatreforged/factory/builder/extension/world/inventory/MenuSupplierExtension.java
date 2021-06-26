package com.combatreforged.factory.builder.extension.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface MenuSupplierExtension<T extends AbstractContainerMenu> {
    T createServer(int i, Inventory inventory);
}
