package com.combatreforged.factory.builder.mixin.world.effect;

import com.combatreforged.factory.builder.implementation.util.ConversionTables;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffects.class)
public abstract class MobEffectsMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void loadConversionTable(CallbackInfo ci) {
        ConversionTables.setupEffects();
    }
}
