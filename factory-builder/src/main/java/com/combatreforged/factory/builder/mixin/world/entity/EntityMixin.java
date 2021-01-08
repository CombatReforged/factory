package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import net.minecraft.commands.CommandSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*xRot = pitch
yRot = yaw*/


@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, CommandSource, Wrap<com.combatreforged.factory.api.world.entity.Entity> {
    private com.combatreforged.factory.api.world.entity.Entity wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(EntityType<?> entityType, Level level, CallbackInfo ci) {
        this.wrapped = new WrappedEntity((Entity) (Object) this);
    }

    @Override
    public com.combatreforged.factory.api.world.entity.Entity wrap() {
        return this.wrapped;
    }
}
