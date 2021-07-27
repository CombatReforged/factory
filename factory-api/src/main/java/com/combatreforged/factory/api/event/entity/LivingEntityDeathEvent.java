package com.combatreforged.factory.api.event.entity;

import com.combatreforged.factory.api.event.Event;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.damage.DamageData;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.item.ItemStack;

import java.util.Set;

public class LivingEntityDeathEvent implements Event {
    public static final EventBackend<LivingEntityDeathEvent> BACKEND = EventBackend.create(LivingEntityDeathEvent.class);

    private final LivingEntity entity;
    private Set<ItemStack> drops;
    private DamageData cause;

    public LivingEntityDeathEvent(LivingEntity entity, DamageData cause, Set<ItemStack> drops) {
        this.entity = entity;
        this.cause = cause;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageData getCause() {
        return cause;
    }

    public void setCause(DamageData cause) {
        this.cause = cause;
    }

    public Set<ItemStack> getDrops() {
        return drops;
    }

    public void setDrops(Set<ItemStack> drops) {
        this.drops = drops;
    }
}
