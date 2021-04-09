package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.world.item.Item;

public interface Block {
    boolean isAir();
    Item getDrop();
    <T> T getPropertyValue(StateProperty stateProperty);
    <T> void setPropertyValue(StateProperty stateProperty, T value);

    enum StateProperty {
        ; //TODO add properties

        private final Class<?> valueType;
        StateProperty(Class<?> valueType) {
            this.valueType = valueType;
        }

        public Class<?> getValueType() {
            return valueType;
        }

        enum Direction {
            NORTH(true),
            EAST(true),
            SOUTH(true),
            WEST(true),
            UP(false),
            DOWN(false);

            private final boolean horizontal;

            Direction(boolean horizontal) {
                this.horizontal = horizontal;
            }

            public boolean isHorizontal() {
                return this.horizontal;
            }
        }

        //TODO add enums

    }

}
