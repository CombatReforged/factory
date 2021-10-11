package com.combatreforged.factory.builder.mixin._bugfixes;

import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface ConnectionAccessor {
    @Accessor
    boolean getDisconnectionHandled();
}
