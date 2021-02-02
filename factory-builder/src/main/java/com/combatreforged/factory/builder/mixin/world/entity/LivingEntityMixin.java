package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Wrap<com.combatreforged.factory.api.world.entity.LivingEntity> {
    private com.combatreforged.factory.api.world.entity.LivingEntity wrapped;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(EntityType<? extends LivingEntity> entityType, Level level, CallbackInfo ci) {
        this.wrapped = new WrappedLivingEntity((LivingEntity) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.entity.LivingEntity wrap() {
        return this.wrapped;
    }
}
