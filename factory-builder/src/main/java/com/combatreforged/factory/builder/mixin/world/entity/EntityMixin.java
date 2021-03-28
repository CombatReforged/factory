package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.builder.extension.EntityExtension;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.entity.projectile.WrappedProjectile;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*xRot = pitch
yRot = yaw*/


@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, CommandSource, Wrap<com.combatreforged.factory.api.world.entity.Entity>, EntityExtension {
    private com.combatreforged.factory.api.world.entity.Entity wrapped;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void injectWrapped(EntityType<?> entityType, Level level, CallbackInfo ci) {
        this.wrapped = createWrapped((Entity) (Object) this);
    }

    public WrappedEntity createWrapped(Entity entity) {
        Class<? extends Entity> clazz = entity.getClass();
        if (entity instanceof LivingEntity) {
            if (clazz == ServerPlayer.class) {
                return new WrappedPlayer((ServerPlayer) entity);
            }
            return new WrappedLivingEntity((LivingEntity) entity);
        }
        if (entity instanceof Projectile) {
            return new WrappedProjectile((Projectile) entity);
        }

        return new WrappedEntity(entity);
    }

    @Override
    public com.combatreforged.factory.api.world.entity.Entity wrap() {
        return this.wrapped;
    }

    @Invoker
    public int getPermissionLevel() {
        return 0;
    }
}
