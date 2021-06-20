package com.combatreforged.factory.builder.mixin.world.item;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.item.WrappedItemStack;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements Wrap<com.combatreforged.factory.api.world.item.ItemStack> {
    private WrappedItemStack wrapped;
    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedItemStack((ItemStack) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.item.ItemStack wrap() {
        return wrapped;
    }
}
