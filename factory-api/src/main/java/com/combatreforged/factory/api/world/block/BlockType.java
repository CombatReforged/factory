package com.combatreforged.factory.api.world.block;

public interface BlockType {
    Properties getProperties();

    class Properties {
        private boolean mutable = true;

        private boolean rotatable = false;
        private boolean waterloggable = false;
        private RotationAxis[] possibleRotations = new RotationAxis[0];

        private Properties() {
        }

        public static Properties create() {
            return new Properties();
        }

        public Properties rotatable(boolean rotatable) {
            if (mutable)
                this.rotatable = rotatable;
            return this;
        }

        public Properties waterloggable(boolean waterloggable) {
            if (mutable)
                this.waterloggable = waterloggable;
            return this;
        }

        public Properties possibleRotations(RotationAxis[] rotationAxes) {
            if (mutable)
                this.possibleRotations = rotationAxes;
            return this;
        }

        public Properties makeImmutable() {
            this.mutable = false;
            return this;
        }

        public boolean isRotatable() {
            return rotatable;
        }

        public boolean isMutable() {
            return mutable;
        }

        public boolean isWaterloggable() {
            return waterloggable;
        }

        public RotationAxis[] getPossibleRotations() {
            return possibleRotations;
        }
    }

    enum RotationAxis {
        HORIZONTAL, VERTICAL
    }
}
