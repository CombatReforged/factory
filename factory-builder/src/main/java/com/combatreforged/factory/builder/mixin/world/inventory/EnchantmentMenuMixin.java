package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.implementation.util.BlockDependentMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin implements BlockDependentMenu {
    @Mutable
    @Shadow @Final private ContainerLevelAccess access;
    private ContainerLevelAccess prevAccess;

    private Player player;

    private boolean independent;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void injectCustomContainerLevelAccess(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        this.independent = false;
        this.prevAccess = null;
        this.player = inventory.player;

        if (this.access == ContainerLevelAccess.NULL) {
            this.setIndependent(true);
        }
    }

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    public void enableIndependent(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.independent) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void setIndependent(boolean independent) {
        this.independent = independent;
        if (independent) {
            this.prevAccess = this.access;
            this.access = ContainerLevelAccess.create(player.level, player.blockPosition());
        }
        else {
            this.access = prevAccess;
            this.prevAccess = null;
        }
    }

    @Override
    public boolean isIndependent() {
        return independent;
    }

    //TODO add enchantment level customization
}
