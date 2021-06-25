package com.combatreforged.factory.api.util;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.interfaces.Namespaced;
import com.combatreforged.factory.api.world.block.BlockType;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.item.Enchantment;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenuType;

public interface ImplementationUtils {
    int getMaxLevel(Enchantment enchantment);
    boolean isCurse(Enchantment enchantment);
    boolean canApply(Enchantment enchantment, ItemStack itemStack);

    StatusEffect.Type getType(StatusEffect effect);

    boolean isBlockItem(ItemType item);
    BlockType getBlock(ItemType item);
    int getMaxStackSize(ItemType item);
    int getMaxDamage(ItemType item);

    Identifier getIdentifier(Namespaced namespaced);

    ContainerMenu<?> createMenu(ContainerMenuType menuType, int containerID, PlayerInventory inventory);

    static ImplementationUtils getInstance() {
        return FactoryAPI.getInstance().getImplementationUtils();
    }
}
