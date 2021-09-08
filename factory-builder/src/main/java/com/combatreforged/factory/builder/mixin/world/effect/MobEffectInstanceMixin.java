package com.combatreforged.factory.builder.mixin.world.effect;

import com.combatreforged.factory.api.world.effect.StatusEffectInstance;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.effect.WrappedStatusEffectInstance;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements Wrap<StatusEffectInstance> {
    private StatusEffectInstance wrapped;

    @Inject(method = { "<init>(Lnet/minecraft/world/effect/MobEffect;IIZZZLnet/minecraft/world/effect/MobEffectInstance;)V", "<init>(Lnet/minecraft/world/effect/MobEffect;)V" }, at = @At("TAIL"))
    public void injectWrap(CallbackInfo ci) {
        this.wrapped = new WrappedStatusEffectInstance((MobEffectInstance) (Object) this);
    }

    @Override
    public StatusEffectInstance wrap() {
        return wrapped;
    }
}
