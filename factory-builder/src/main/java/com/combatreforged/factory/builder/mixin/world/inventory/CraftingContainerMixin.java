package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedCraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingContainer.class)
public abstract class CraftingContainerMixin implements Wrap<com.combatreforged.factory.api.world.item.container.CraftingContainer> {
    private WrappedCraftingContainer wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(AbstractContainerMenu abstractContainerMenu, int i, int j, CallbackInfo ci) {
        this.wrapped = new WrappedCraftingContainer((CraftingContainer) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.item.container.CraftingContainer wrap() {
        return wrapped;
    }
}
