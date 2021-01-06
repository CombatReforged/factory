package com.combatreforged.factory.api.world.damage;

import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.LivingEntity;

public class DamageData {
    private Type damageType;
    private Entity damageSource;
    private LivingEntity projectileOwner;

    private DamageData() {}

    public static class Builder {
        private final DamageData damageData;

        private Builder() {
            this.damageData = new DamageData();
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder damageType(Type type) {
            this.damageData.damageType = type;
            return this;
        }

        public Builder damageSource(Entity entity) {
            this.damageData.damageSource = entity;
            return this;
        }

        public Builder projectileOwner(LivingEntity livingEntity) {
            this.damageData.projectileOwner = livingEntity;
            return this;
        }

        public DamageData build() {
            return this.damageData;
        }
    }

    public enum Type {
        GENERIC, PROJECTILE, EXPLOSION, FIRE, THORNS, MAGIC, SUFFOCATION;
    }
}
