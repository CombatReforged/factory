package com.combatreforged.factory.builder.mixin.world.entity.player;

import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedPlayerInventory;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin implements Wrap<PlayerInventory> {
    private WrappedPlayerInventory wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = new WrappedPlayerInventory((Inventory) (Object) this);
    }

    @Override
    public PlayerInventory wrap() {
        return wrapped;
    }
}
