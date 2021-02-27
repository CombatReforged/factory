package com.combatreforged.factory.api.world.effect;

public class StatusEffect {
    public static final StatusEffect 
            SWIFTNESS = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            SLOWNESS = new StatusEffect(StatusEffect.Type.HARMFUL),
            HASTE = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            MINING_FATIGUE = new StatusEffect(StatusEffect.Type.HARMFUL),
            STRENGTH = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            WEAKNESS = new StatusEffect(StatusEffect.Type.HARMFUL),
            INSTANT_HEALTH = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            INSTANT_DAMAGE = new StatusEffect(StatusEffect.Type.HARMFUL),
            JUMP_BOOST = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            NAUSEA = new StatusEffect(StatusEffect.Type.HARMFUL),
            REGENERATION = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            POISON = new StatusEffect(StatusEffect.Type.HARMFUL),
            RESISTANCE = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            FIRE_RESISTANCE = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            WATER_BREATHING = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            INVISIBILITY = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            BLINDNESS = new StatusEffect(StatusEffect.Type.HARMFUL),
            NIGHT_VISION = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            SATURATION = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            HUNGER = new StatusEffect(StatusEffect.Type.HARMFUL),
            WITHER = new StatusEffect(StatusEffect.Type.HARMFUL),
            HEALTH_BOOST = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            ABSORPTION = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            GLOWING = new StatusEffect(StatusEffect.Type.NEUTRAL),
            LEVITATION = new StatusEffect(StatusEffect.Type.HARMFUL),
            LUCK = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            UNLUCK = new StatusEffect(StatusEffect.Type.HARMFUL),
            SLOW_FALLING = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            CONDUIT_POWER = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            DOLPHINS_GRACE = new StatusEffect(StatusEffect.Type.BENEFICIAL),
            BAD_OMEN = new StatusEffect(StatusEffect.Type.NEUTRAL),
            HERO_OF_THE_VILLAGE = new StatusEffect(StatusEffect.Type.BENEFICIAL);

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
