package com.combatreforged.factory.builder.mixin.world.damagesource;

import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.damage.WrappedDamageData;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements Wrap<DamageData> {
    private DamageData wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrap(String string, CallbackInfo ci) {
        this.wrapped = new WrappedDamageData((DamageSource) (Object) this);
    }

    @Override
    public DamageData wrap() {
        return wrapped;
    }
}
