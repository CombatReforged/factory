package com.combatreforged.factory.builder.mixin.world.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceMenu.class)
public interface AbstractFurnaceMenuAccessor {
    @Accessor
    Container getContainer();
}
