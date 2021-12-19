package com.combatreforged.factory.api.event.entity;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.effect.StatusEffect;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LivingEntityHealEvent extends LivingEntityEvent implements Cancellable {
    public static final EventBackend<LivingEntityHealEvent> BACKEND = EventBackend.create(LivingEntityHealEvent.class);

    private boolean cancelled;
    private float amount;
    private final HealCause cause;

    public LivingEntityHealEvent(LivingEntity entity, float amount, HealCause cause) {
        super(entity);
        this.amount = amount;
        this.cause = cause;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public HealCause getCause() {
        return cause;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    public static class HealCause {
        public static final HealCause NATURAL_REGENERATION = new HealCause();

        public static HealCause effect(StatusEffect effect) {
            return new HealCause(effect);
        }

        private final @Nullable StatusEffect effect;
        private HealCause() {
            this.effect = null;
        }

        private HealCause(@Nullable StatusEffect effect) {
            this.effect = effect;
        }

        public @Nullable StatusEffect getEffect() {
            return effect;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HealCause)) return false;
            HealCause healCause = (HealCause) o;
            return Objects.equals(effect, healCause.effect);
        }

        @Override
        public int hashCode() {
            return Objects.hash(effect);
        }
    }
}
