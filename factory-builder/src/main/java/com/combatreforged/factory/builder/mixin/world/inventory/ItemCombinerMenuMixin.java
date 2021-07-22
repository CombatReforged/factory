package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.mixin_interfaces.BlockDependentMenu;
import com.combatreforged.factory.builder.mixin_interfaces.LevelAccessOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemCombinerMenu.class)
public abstract class ItemCombinerMenuMixin implements BlockDependentMenu, LevelAccessOwner {
    private ContainerLevelAccess prevAccess;

    private Player player;

    private boolean independent;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectCustomContainerLevelAccess(MenuType<?> menuType, int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        this.independent = false;
        this.prevAccess = null;
        this.player = inventory.player;

        if (this.getContainerLevelAccess() == ContainerLevelAccess.NULL) {
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
            this.prevAccess = this.getContainerLevelAccess();
            this.setContainerLevelAccess(ContainerLevelAccess.create(player.level, BlockPos.ZERO));
        } else {
            this.setContainerLevelAccess(prevAccess);
            this.prevAccess = null;
        }
    }

    @Override
    public boolean isIndependent() {
        return independent;
    }
}
