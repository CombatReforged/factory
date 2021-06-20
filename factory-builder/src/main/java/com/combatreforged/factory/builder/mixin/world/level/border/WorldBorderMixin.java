package com.combatreforged.factory.builder.mixin.world.level.border;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.border.WrappedWorldBorder;
import net.minecraft.world.level.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorder.class)
public abstract class WorldBorderMixin implements Wrap<com.combatreforged.factory.api.world.border.WorldBorder> {
    private WrappedWorldBorder wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        wrapped = new WrappedWorldBorder((WorldBorder) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.border.WorldBorder wrap() {
        return wrapped;
    }
}
