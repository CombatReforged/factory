package com.combatreforged.factory.builder.mixin.world.entity;

import com.combatreforged.factory.builder.extension.world.entity.EntityExtension;
import com.combatreforged.factory.builder.extension.wrap.ChangeableWrap;
import com.combatreforged.factory.builder.extension.wrap.Wrap;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedAgeable;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedMob;
import com.combatreforged.factory.builder.implementation.world.entity.animal.WrappedAnimal;
import com.combatreforged.factory.builder.implementation.world.entity.monster.WrappedMonster;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.combatreforged.factory.builder.implementation.world.entity.projectile.WrappedProjectile;
import net.minecraft.commands.CommandSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*xRot = pitch
yRot = yaw*/


@Mixin(Entity.class)
public abstract class EntityMixin implements Nameable, CommandSource, ChangeableWrap<com.combatreforged.factory.api.world.entity.Entity>, EntityExtension {
    private com.combatreforged.factory.api.world.entity.Entity wrapped;

    @Inject(method = "<init>*", at = @At("TAIL"))
    public void injectWrapped(CallbackInfo ci) {
        this.wrapped = createWrapped((Entity) (Object) this);
    }

    public WrappedEntity createWrapped(Entity entity) {
        if (entity instanceof LivingEntity) {
            if (entity instanceof Mob) {
                if (entity instanceof Monster) {
                    return new WrappedMonster((Monster) entity);
                }
                if (entity instanceof AgableMob) {
                    if (entity instanceof Animal) {
                        return new WrappedAnimal((Animal) entity);
                    }
                    return new WrappedAgeable((AgableMob) entity);
                }
                return new WrappedMob((Mob) entity);
            }
            if (entity instanceof ServerPlayer) {
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

    @Override
    public void setWrap(com.combatreforged.factory.api.world.entity.Entity wrap) {
        this.wrapped = wrap;
    }

    @Invoker @Override
    public abstract int invokeGetPermissionLevel();
}
