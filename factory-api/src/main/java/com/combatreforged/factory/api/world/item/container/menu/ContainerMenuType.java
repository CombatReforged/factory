package com.combatreforged.factory.api.world.item.container.menu;

import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;

public interface ContainerMenuType extends ObjectMapped {
    ContainerMenu<?> createMenu(int containerID, PlayerInventory inventory);
}
