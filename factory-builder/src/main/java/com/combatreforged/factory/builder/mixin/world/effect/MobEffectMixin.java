package com.combatreforged.factory.builder.mixin.world.effect;

import com.combatreforged.factory.builder.extension.world.effect.MobEffectExtension;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MobEffect.class)
public abstract class MobEffectMixin implements MobEffectExtension {
    @Shadow @Final private MobEffectCategory category;

    @Override
    public MobEffectCategory getCategory() {
        return category;
    }
}
