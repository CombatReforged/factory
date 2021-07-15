package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.mixin.world.level.block.entity.BeaconBlockEntityAccessor;
import com.combatreforged.factory.builder.mixin_interfaces.BlockDependentMenu;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconMenu.class)
public abstract class BeaconMenuMixin implements BlockDependentMenu {
    @Mutable @Shadow @Final private ContainerData beaconData;
    @Mutable @Shadow @Final private ContainerLevelAccess access;
    @Shadow @Final private Container beacon;
    private boolean independent;
    private ContainerData prevContainerData;
    private ContainerLevelAccess prevAccess;
    private Player player;

    @Inject(method = "<init>(ILnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
    public void injectCustomContainerData(int i, Container container, ContainerData containerData, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
        this.independent = false;
        this.prevContainerData = null;
        this.prevAccess = null;
        this.player = container instanceof Inventory ? ((Inventory) container).player : null;

        if (this.player != null && containerData instanceof SimpleContainerData) {
            this.setIndependent(true);
        }
    }

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    public void enableIndependence(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.independent) {
            cir.setReturnValue(true);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setIndependent(boolean independent) {
        if (player != null) {
            this.independent = independent;
            if (independent) {
                this.prevContainerData = this.beaconData;
                this.prevAccess = this.access;

                BeaconBlockEntity beaconBlockEntity = new BeaconBlockEntity();
                this.beaconData = ((BeaconBlockEntityAccessor) beaconBlockEntity).getDataAccess();
                this.access = ContainerLevelAccess.create(player.level, player.blockPosition());
                player.level.addBlockEntity(beaconBlockEntity);
            } else {
                if (this.beacon instanceof BeaconBlockEntity) {
                    ((BeaconBlockEntity) this.beacon).setRemoved();
                }
                this.beaconData = this.prevContainerData;
                this.access = this.prevAccess;
            }
        }
    }

    @Override
    public boolean isIndependent() {
        return independent;
    }
}
