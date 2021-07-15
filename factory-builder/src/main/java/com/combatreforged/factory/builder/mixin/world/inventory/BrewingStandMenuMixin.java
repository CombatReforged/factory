package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.mixin.world.level.block.entity.BrewingStandBlockEntityAccessor;
import com.combatreforged.factory.builder.mixin_interfaces.BlockDependentMenu;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandMenu.class)
public abstract class BrewingStandMenuMixin implements BlockDependentMenu {
    @Mutable @Shadow @Final private Container brewingStand;
    @Mutable @Shadow @Final private ContainerData brewingStandData;
    private Container prevBrewingStand;
    private ContainerData prevBrewingStandData;
    private boolean independent;
    private Level level;

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;)V", at = @At("TAIL"))
    public void injectCustomContainer(int i, Inventory inventory, Container container, ContainerData containerData, CallbackInfo ci) {
        this.independent = false;
        this.prevBrewingStand = null;
        this.prevBrewingStandData = null;
        this.level = inventory.player.level;

        if (container instanceof SimpleContainer) {
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
        if (independent) {
            this.prevBrewingStand = this.brewingStand;
            this.prevBrewingStandData = this.brewingStandData;

            BrewingStandBlockEntity blockEntity = new BrewingStandBlockEntity();
            this.brewingStand = blockEntity;
            this.brewingStandData = ((BrewingStandBlockEntityAccessor) this.brewingStand).getDataAccess();
            this.level.addBlockEntity(blockEntity);
        } else {
            if (this.brewingStand instanceof BrewingStandBlockEntity) {
                ((BrewingStandBlockEntity) this.brewingStand).setRemoved();
            }
            this.brewingStand = this.prevBrewingStand;
            this.brewingStandData = this.prevBrewingStandData;
        }
        this.independent = independent;
    }

    @Override
    public boolean isIndependent() {
        return independent;
    }
}
