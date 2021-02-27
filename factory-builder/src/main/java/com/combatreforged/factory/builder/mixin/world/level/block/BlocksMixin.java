package com.combatreforged.factory.builder.mixin.world.level.block;

import com.combatreforged.factory.builder.implementation.util.ConversionTables;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void loadConversionTable(CallbackInfo ci) {
        ConversionTables.setupBlocks();
    }
}
