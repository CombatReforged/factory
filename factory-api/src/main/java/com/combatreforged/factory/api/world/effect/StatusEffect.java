package com.combatreforged.factory.api.world.effect;

import java.util.HashMap;
import java.util.Map;

public class StatusEffect {
    public static final StatusEffect SWIFTNESS = new StatusEffect("minecraft:speed", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect SLOWNESS = new StatusEffect("minecraft:slowness", StatusEffect.Type.HARMFUL);
    public static final StatusEffect HASTE = new StatusEffect("minecraft:haste", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect MINING_FATIGUE = new StatusEffect("minecraft:mining_fatigue", StatusEffect.Type.HARMFUL);
    public static final StatusEffect STRENGTH = new StatusEffect("minecraft:strength", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect WEAKNESS = new StatusEffect("minecraft:weakness", StatusEffect.Type.HARMFUL);
    public static final StatusEffect INSTANT_HEALTH = new StatusEffect("minecraft:instant_health", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect INSTANT_DAMAGE = new StatusEffect("minecraft:instant_damage", StatusEffect.Type.HARMFUL);
    public static final StatusEffect JUMP_BOOST = new StatusEffect("minecraft:jump_boost", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect NAUSEA = new StatusEffect("minecraft:nausea", StatusEffect.Type.HARMFUL);
    public static final StatusEffect REGENERATION = new StatusEffect("minecraft:regeneration", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect POISON = new StatusEffect("minecraft:poison", StatusEffect.Type.HARMFUL);
    public static final StatusEffect RESISTANCE = new StatusEffect("minecraft:resistance", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect FIRE_RESISTANCE = new StatusEffect("minecraft:fire_resistance", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect WATER_BREATHING = new StatusEffect("minecraft:water_breathing", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect INVISIBILITY = new StatusEffect("minecraft:invisibility", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect BLINDNESS = new StatusEffect("minecraft:blindness", StatusEffect.Type.HARMFUL);
    public static final StatusEffect NIGHT_VISION = new StatusEffect("minecraft:night_vision", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect SATURATION = new StatusEffect("minecraft:saturation", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect HUNGER = new StatusEffect("minecraft:hunger", StatusEffect.Type.HARMFUL);
    public static final StatusEffect WITHER = new StatusEffect("minecraft:wither", StatusEffect.Type.HARMFUL);
    public static final StatusEffect HEALTH_BOOST = new StatusEffect("minecraft:health_boost", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect ABSORPTION = new StatusEffect("minecraft:absorption", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect GLOWING = new StatusEffect("minecraft:glowing", StatusEffect.Type.NEUTRAL);
    public static final StatusEffect LEVITATION = new StatusEffect("minecraft:levitation", StatusEffect.Type.HARMFUL);
    public static final StatusEffect LUCK = new StatusEffect("minecraft:luck", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect UNLUCK = new StatusEffect("minecraft:unluck", StatusEffect.Type.HARMFUL);
    public static final StatusEffect SLOW_FALLING = new StatusEffect("minecraft:slow_falling", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect CONDUIT_POWER = new StatusEffect("minecraft:conduit_power", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect DOLPHINS_GRACE = new StatusEffect("minecraft:dolphins_grace", StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect BAD_OMEN = new StatusEffect("minecraft:bad_omen", StatusEffect.Type.NEUTRAL);
    public static final StatusEffect HERO_OF_THE_VILLAGE = new StatusEffect("minecraft:hero_of_the_village", StatusEffect.Type.BENEFICIAL);


    private static final Map<String, StatusEffect> registered = new HashMap<>();

    public static Map<String, StatusEffect> getRegisteredEffects() {
        return new HashMap<>(registered);
    }

    private final String id;
    private final Type type;

    public StatusEffect(String id, Type type) {
        this.id = id;
        this.type = type;

        registered.put(id, this);
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL;
    }
}
