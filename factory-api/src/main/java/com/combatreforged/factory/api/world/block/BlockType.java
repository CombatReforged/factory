package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.interfaces.StringIdentified;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;

@Immutable
public interface BlockType {
    Properties getProperties();

    @Immutable
    class Properties {
        public static class Builder {
            protected boolean rotatable = false;
            protected boolean waterloggable = false;
            protected RotationAxis[] possibleRotations = new RotationAxis[0];

            public static Builder create() {
                return new Builder();
            }

            public Builder rotatable(boolean rotatable) {
                this.rotatable = rotatable;
                return this;
            }

            public Builder waterloggable(boolean waterloggable) {
                this.waterloggable = waterloggable;
                return this;
            }

            public Builder possibleRotations(RotationAxis[] rotationAxes) {
                this.possibleRotations = rotationAxes;
                return this;
            }

            public Properties build() {
                return new Properties(this.rotatable, this.waterloggable, this.possibleRotations);
            }
        }

        protected final boolean rotatable;
        protected final boolean waterloggable;
        protected final ImmutableList<RotationAxis> possibleRotations;

        private Properties() {
            this.rotatable = false;
            this.waterloggable = false;
            this.possibleRotations = ImmutableList.copyOf(new RotationAxis[0]);
        }

        private Properties(boolean rotatable, boolean waterloggable, RotationAxis[] possibleRotations) {
            this.rotatable = rotatable;
            this.waterloggable = waterloggable;
            this.possibleRotations = ImmutableList.copyOf(possibleRotations);
        }

        public boolean isRotatable() {
            return rotatable;
        }

        public boolean isWaterloggable() {
            return waterloggable;
        }

        public RotationAxis[] getPossibleRotations() {
            return possibleRotations.toArray(new RotationAxis[0]);
        }

        public Properties copy() {
            return new Properties(this.rotatable, this.waterloggable, this.possibleRotations.toArray(new RotationAxis[0]));
        }
    }

    enum RotationAxis {
        HORIZONTAL, VERTICAL
    }

    @Immutable
    abstract class Other implements BlockType, StringIdentified {
    }
}
