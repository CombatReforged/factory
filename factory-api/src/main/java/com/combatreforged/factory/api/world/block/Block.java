package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.world.item.Item;

import java.util.function.Predicate;

public interface Block {
    boolean isAir();

    Item getDrop();

    <T> T getPropertyValue(StateProperty stateProperty);

    <T> void setPropertyValue(StateProperty stateProperty, T value);

    public enum StateProperty {
        ATTACHED(Boolean.class),
        BOTTOM(Boolean.class),
        CONDITIONAL(Boolean.class),
        DISARMED(Boolean.class),
        DRAG(Boolean.class),
        ENABLED(Boolean.class),
        EXTENDED(Boolean.class),
        EYE(Boolean.class),
        FALLING(Boolean.class),
        HANGING(Boolean.class),
        HAS_BOTTLE_0(Boolean.class),
        HAS_BOTTLE_1(Boolean.class),
        HAS_BOTTLE_2(Boolean.class),
        HAS_RECORD(Boolean.class),
        HAS_BOOK(Boolean.class),
        INVERTED(Boolean.class),
        IN_WALL(Boolean.class),
        LIT(Boolean.class),
        LOCKED(Boolean.class),
        OCCUPIED(Boolean.class),
        OPEN(Boolean.class),
        PERSISTENT(Boolean.class),
        POWERED(Boolean.class),
        SHORT(Boolean.class),
        SIGNAL_FIRE(Boolean.class),
        SNOWY(Boolean.class),
        TRIGGERED(Boolean.class),
        UNSTABLE(Boolean.class),
        WATERLOGGED(Boolean.class),
        VINE_END(Boolean.class),
        HORIZONTAL_AXIS(Axis.class, Axis::isHorizontal),
        AXIS(Axis.class),
        UP(Boolean.class),
        DOWN(Boolean.class),
        NORTH(Boolean.class),
        EAST(Boolean.class),
        SOUTH(Boolean.class),
        WEST(Boolean.class),
        FACING(Direction.class),
        FACING_HOPPER(Direction.class, direction -> direction != Direction.UP),
        HORIZONTAL_FACING(Direction.class, Direction::isHorizontal),
        ORIENTATION(FrontAndTop.class),
        ATTACH_FACE(AttachFace.class),
        BELL_ATTACHMENT(BellAttachType.class),
        EAST_WALL(WallSide.class),
        NORTH_WALL(WallSide.class),
        SOUTH_WALL(WallSide.class),
        WEST_WALL(WallSide.class),
        EAST_REDSTONE(RedstoneSide.class),
        NORTH_REDSTONE(RedstoneSide.class),
        SOUTH_REDSTONE(RedstoneSide.class),
        WEST_REDSTONE(RedstoneSide.class),
        DOUBLE_BLOCK_HALF(DoubleBlockHalf.class),
        HALF(Half.class),
        RAIL_SHAPE(RailShape.class),
        RAIL_SHAPE_STRAIGHT(RailShape.class),
        AGE_1(Integer.class),
        AGE_2(Integer.class),
        AGE_3(Integer.class),
        AGE_5(Integer.class),
        AGE_7(Integer.class),
        AGE_15(Integer.class),
        AGE_25(Integer.class),
        BITES(Integer.class),
        DELAY(Integer.class),
        DISTANCE(Integer.class),
        EGGS(Integer.class),
        HATCH(Integer.class),
        LAYERS(Integer.class),
        LEVEL_CAULDRON(Integer.class),
        LEVEL_COMPOSTER(Integer.class),
        LEVEL_FLOWING(Integer.class),
        LEVEL_HONEY(Integer.class),
        LEVEL(Integer.class),
        MOISTURE(Integer.class),
        NOTE(Integer.class),
        PICKLES(Integer.class),
        POWER(Integer.class),
        STAGE(Integer.class),
        STABILITY_DISTANCE(Integer.class),
        RESPAWN_ANCHOR_CHARGES(Integer.class),
        ROTATION_16(Integer.class),
        BED_PART(BedPart.class),
        CHEST_TYPE(ChestType.class),
        MODE_COMPARATOR(ComparatorMode.class),
        DOOR_HINGE(DoorHingeSide.class),
        NOTEBLOCK_INSTRUMENT(NoteBlockInstrument.class),
        PISTON_TYPE(PistonType.class),
        SLAB_TYPE(SlabType.class),
        STAIRS_SHAPE(StairsShape.class),
        STRUCTUREBLOCK_MODE(StructureMode.class),
        BAMBOO_LEAVES(BambooLeaves.class);

        private final Class<?> valueType;
        private final Predicate<Object> allowedCheck;

        <T> StateProperty(Class<T> valueType) {
            this(valueType, value -> true);
        }

        @SuppressWarnings("unchecked")
        <T> StateProperty(Class<T> valueType, Predicate<T> allowedCheck) {
            this.valueType = valueType;
            this.allowedCheck = value -> allowedCheck.test((T) value);
        }

        public Class<?> getValueType() {
            return valueType;
        }

        public <T> boolean isAllowed(T value) {
            return allowedCheck.test(value);
        }

        public interface StatePropertyEnum {}

        public enum Direction implements StatePropertyEnum {
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

        public enum Axis implements StatePropertyEnum {
            X(true), Y(false), Z(true);

            private final boolean horizontal;

            Axis(boolean horizontal) {
                this.horizontal = horizontal;
            }

            public boolean isHorizontal() {
                return this.horizontal;
            }
        }

        public enum FrontAndTop implements StatePropertyEnum {
            DOWN_EAST(Direction.DOWN, Direction.EAST),
            DOWN_NORTH(Direction.DOWN, Direction.NORTH),
            DOWN_SOUTH(Direction.DOWN, Direction.SOUTH),
            DOWN_WEST(Direction.DOWN, Direction.WEST),
            UP_EAST(Direction.UP, Direction.EAST),
            UP_NORTH(Direction.UP, Direction.NORTH),
            UP_SOUTH(Direction.UP, Direction.SOUTH),
            UP_WEST(Direction.UP, Direction.WEST),
            WEST_UP(Direction.WEST, Direction.UP),
            EAST_UP(Direction.EAST, Direction.UP),
            NORTH_UP(Direction.NORTH, Direction.UP),
            SOUTH_UP(Direction.SOUTH, Direction.UP);

            private final Direction front;
            private final Direction top;

            FrontAndTop(Direction front, Direction top) {
                this.front = front;
                this.top = top;
            }

            public Direction getFront() {
                return front;
            }

            public Direction getTop() {
                return top;
            }
        }

        public enum AttachFace implements StatePropertyEnum {
            FLOOR, WALL, CEILING
        }

        public enum BellAttachType implements StatePropertyEnum {
            FLOOR, CEILING, SINGLE_WALL, DOUBLE_WALL
        }

        public enum WallSide implements StatePropertyEnum {
            NONE, LOW, TALL
        }

        public enum BedPart implements StatePropertyEnum {
            HEAD, FOOT
        }

        public enum ChestType implements StatePropertyEnum {
            SINGLE, LEFT, RIGHT
        }

        public enum ComparatorMode implements StatePropertyEnum {
            COMPARE, SUBTRACT
        }

        public enum DoorHingeSide implements StatePropertyEnum {
            LEFT, RIGHT
        }

        public enum NoteBlockInstrument implements StatePropertyEnum {
            HARP,
            BASEDRUM,
            SNARE,
            HAT,
            BASS,
            FLUTE,
            BELL,
            GUITAR,
            CHIME,
            XYLOPHONE,
            IRON_XYLOPHONE,
            COW_BELL,
            DIDGERIDOO,
            BIT,
            BANJO,
            PLING
        }

        public enum PistonType implements StatePropertyEnum {
            NORMAL, STICKY
        }

        public enum SlabType implements StatePropertyEnum {
            TOP, BOTTOM, DOUBLE
        }

        public enum StairsShape implements StatePropertyEnum {
            STRAIGHT,
            INNER_LEFT,
            INNER_RIGHT,
            OUTER_LEFT,
            OUTER_RIGHT
        }

        public enum StructureMode implements StatePropertyEnum {
            LOAD, SAVE, CORNER, DATA
        }

        public enum RedstoneSide implements StatePropertyEnum {
            UP, SIDE, NONE
        }

        public enum DoubleBlockHalf implements StatePropertyEnum {
            UPPER, LOWER
        }

        public enum Half implements StatePropertyEnum {
            TOP, BOTTOM
        }

        public enum RailShape implements StatePropertyEnum {
            NORTH_SOUTH,
            EAST_WEST,
            ASCENDING_EAST,
            ASCENDING_WEST,
            ASCENDING_NORTH,
            ASCENDING_SOUTH,
            SOUTH_EAST,
            SOUTH_WEST,
            NORTH_WEST,
            NORTH_EAST
        }

        public enum BambooLeaves implements StatePropertyEnum {
            NONE, SMALL, LARGE
        }
    }

}
