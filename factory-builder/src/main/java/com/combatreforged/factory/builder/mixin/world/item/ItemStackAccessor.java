package com.combatreforged.factory.builder.mixin.world.item;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.throwables.MixinApplyError;

@Mixin(ItemStack.class)
public interface ItemStackAccessor {
    @Accessor @SuppressWarnings("DoNotCallSuggester")
    static Style getLORE_STYLE() { throw new MixinApplyError("Accessor for LORE_STYLE not applied"); }
}
