package com.combatreforged.factory.api.world.effect;

public class StatusEffect {
    public static final StatusEffect SWIFTNESS = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect SLOWNESS = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect HASTE = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect MINING_FATIGUE = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect STRENGTH = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect WEAKNESS = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect INSTANT_HEALTH = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect INSTANT_DAMAGE = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect JUMP_BOOST = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect NAUSEA = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect REGENERATION = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect POISON = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect RESISTANCE = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect FIRE_RESISTANCE = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect WATER_BREATHING = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect INVISIBILITY = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect BLINDNESS = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect NIGHT_VISION = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect SATURATION = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect HUNGER = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect WITHER = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect HEALTH_BOOST = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect ABSORPTION = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect GLOWING = new StatusEffect(StatusEffect.Type.NEUTRAL);
    public static final StatusEffect LEVITATION = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect LUCK = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect UNLUCK = new StatusEffect(StatusEffect.Type.HARMFUL);
    public static final StatusEffect SLOW_FALLING = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect CONDUIT_POWER = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect DOLPHINS_GRACE = new StatusEffect(StatusEffect.Type.BENEFICIAL);
    public static final StatusEffect BAD_OMEN = new StatusEffect(StatusEffect.Type.NEUTRAL);
    public static final StatusEffect HERO_OF_THE_VILLAGE = new StatusEffect(StatusEffect.Type.BENEFICIAL);

    private final Type type;

    public StatusEffect(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        BENEFICIAL, HARMFUL, NEUTRAL;
    }

    public static class Unidentified extends StatusEffect {
        private final String id;
        public Unidentified(String id, Type type) {
            super(type);
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public boolean equals(Object obj) {
            return obj instanceof Unidentified && obj.getClass() == getClass() && ((Unidentified) obj).getId().equals(this.id);
        }
    }
}
