package com.combatreforged.factory.api.world.effect;

public interface StatusEffectInstance {
    StatusEffect getStatusEffect();
    int getTicksLeft();
    default int getSecondsLeft() {
        return getTicksLeft() / 20;
    }
    int getAmplifier();
    boolean isAmbient();

    class Settings {
        private StatusEffect statusEffect;
        private int duration;
        private int amplifier;
        private boolean ambient;

        public Settings(StatusEffect statusEffect, int duration) {
            this(statusEffect, duration, 0, true);
        }

        public Settings(StatusEffect statusEffect, int duration, int amplifier) {
            this(statusEffect, duration, amplifier, true);
        }

        public Settings(StatusEffect statusEffect, int duration, int amplifier, boolean ambient) {
            this.statusEffect = statusEffect;
            this.duration = duration;
            this.amplifier = amplifier;
            this.ambient = ambient;
        }

        public StatusEffect getStatusEffect() {
            return statusEffect;
        }

        public void setStatusEffect(StatusEffect statusEffect) {
            this.statusEffect = statusEffect;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getAmplifier() {
            return amplifier;
        }

        public void setAmplifier(int amplifier) {
            this.amplifier = amplifier;
        }

        public boolean isAmbient() {
            return ambient;
        }

        public void setAmbient(boolean ambient) {
            this.ambient = ambient;
        }
    }
}
