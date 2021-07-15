package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.mixin.world.level.block.entity.AbstractFurnaceBlockEntityAccessor;
import com.combatreforged.factory.builder.mixin_interfaces.BlockDependentMenu;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin implements BlockDependentMenu {
    @Mutable @Shadow @Final private Container container;
    @Mutable @Shadow @Final private ContainerData data;
    @Shadow @Final private RecipeType<? extends AbstractCookingRecipe> recipeType;
    private Container prevContainer;
    private ContainerData prevData;
    private boolean independent;
    private Level level;

    @Inject(method = "<init>(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/inventory/RecipeBookType;ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;)V", at = @At("TAIL"))
    public void injectCustomContainer(MenuType<?> menuType, RecipeType<? extends AbstractCookingRecipe> recipeType, RecipeBookType recipeBookType, int i, Inventory inventory, Container container, ContainerData containerData, CallbackInfo ci) {
        this.independent = false;
        this.prevContainer = null;
        this.prevData = null;
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

    @Override
    public void setIndependent(boolean independent) {
        if (independent) {
            AbstractFurnaceBlockEntity container = create();
            if (container != null) {
                this.prevContainer = this.container;
                this.prevData = this.data;

                this.container = container;
                this.data = ((AbstractFurnaceBlockEntityAccessor) container).getDataAccess();
                level.addBlockEntity(container);
            }
            else {
                return;
            }
        } else {
            if (this.container instanceof AbstractFurnaceBlockEntity) {
                ((AbstractFurnaceBlockEntity) this.container).setRemoved();
            }
            this.container = this.prevContainer;
            this.data = this.prevData;
        }
        this.independent = independent;
    }

    @Override
    public boolean isIndependent() {
        return independent;
    }

    private AbstractFurnaceBlockEntity create() {
        if (this.recipeType == RecipeType.BLASTING) {
            return new BlastFurnaceBlockEntity();
        }
        else if (this.recipeType == RecipeType.SMOKING) {
            return new SmokerBlockEntity();
        }
        else if (this.recipeType == RecipeType.SMELTING) {
            return new FurnaceBlockEntity();
        }
        return null;
    }

    //TODO fix
}
