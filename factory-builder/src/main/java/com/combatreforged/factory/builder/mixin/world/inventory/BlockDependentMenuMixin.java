package com.combatreforged.factory.builder.mixin.world.inventory;

import com.combatreforged.factory.builder.implementation.util.BlockDependentMenu;
import com.combatreforged.factory.builder.implementation.util.LevelAccessOwner;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class BlockDependentMenuMixin {
    @Mixin(value = {
            EnchantmentMenu.class,
            CraftingMenu.class,
            GrindstoneMenu.class,
            StonecutterMenu.class,
            LoomMenu.class,
            CartographyTableMenu.class,
            ItemCombinerMenu.class
    })
    public static abstract class AllBlockMenusMixin implements BlockDependentMenu, LevelAccessOwner {
        private ContainerLevelAccess prevAccess;

        private Player player;

        private boolean independent;

        @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("TAIL"))
        public void injectCustomContainerLevelAccess(int i, Inventory inventory, ContainerLevelAccess containerLevelAccess, CallbackInfo ci) {
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
                this.setContainerLevelAccess(ContainerLevelAccess.create(player.level, player.blockPosition()));
            }
            else {
                this.setContainerLevelAccess(prevAccess);
                this.prevAccess = null;
            }
        }

        @Override
        public boolean isIndependent() {
            return independent;
        }
    }


    //TODO add enchantment level customization

    @Mixin(CraftingMenu.class)
    public static class CraftingMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(EnchantmentMenu.class)
    public static class EnchantmentMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(GrindstoneMenu.class)
    public static class GrindstoneMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(StonecutterMenu.class)
    public static class StonecutterMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(LoomMenu.class)
    public static class LoomMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(CartographyTableMenu.class)
    public static class CartographyMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        private ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }

    @Mixin(ItemCombinerMenu.class)
    public static class ItemCombinerMenuMixin implements LevelAccessOwner {
        @Mutable
        @Shadow
        @Final
        protected ContainerLevelAccess access;

        @Override
        public void setContainerLevelAccess(ContainerLevelAccess access) {
            this.access = access;
        }

        @Override
        public ContainerLevelAccess getContainerLevelAccess() {
            return access;
        }
    }
}
