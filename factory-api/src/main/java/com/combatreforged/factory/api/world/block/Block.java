package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.interfaces.ObjectMapped;
import com.combatreforged.factory.api.world.item.ItemType;
import com.combatreforged.factory.api.world.util.Location;

import java.util.List;
import java.util.function.Predicate;

public interface Block {
    BlockType getType();

    Location getLocation();

    boolean isAir();

    ItemType getDrop();

    List<StateProperty<?>> getProperties();

    boolean hasPropertyValue(StateProperty<?> stateProperty);

    <T> T getPropertyValue(StateProperty<T> stateProperty);

    <T> void setPropertyValue(StateProperty<T> stateProperty, T value);

    class StateProperty<T> implements ObjectMapped {
        public static final StateProperty<Boolean>
                ATTACHED = new StateProperty<>(Boolean.class),
                BOTTOM = new StateProperty<>(Boolean.class),
                CONDITIONAL = new StateProperty<>(Boolean.class),
                DISARMED = new StateProperty<>(Boolean.class),
                DRAG = new StateProperty<>(Boolean.class),
                ENABLED = new StateProperty<>(Boolean.class),
                EXTENDED = new StateProperty<>(Boolean.class),
                EYE = new StateProperty<>(Boolean.class),
                FALLING = new StateProperty<>(Boolean.class),
                HANGING = new StateProperty<>(Boolean.class),
                HAS_BOTTLE_0 = new StateProperty<>(Boolean.class),
                HAS_BOTTLE_1 = new StateProperty<>(Boolean.class),
                HAS_BOTTLE_2 = new StateProperty<>(Boolean.class),
                HAS_RECORD = new StateProperty<>(Boolean.class),
                HAS_BOOK = new StateProperty<>(Boolean.class),
                INVERTED = new StateProperty<>(Boolean.class),
                IN_WALL = new StateProperty<>(Boolean.class),
                LIT = new StateProperty<>(Boolean.class),
                LOCKED = new StateProperty<>(Boolean.class),
                OCCUPIED = new StateProperty<>(Boolean.class),
                OPEN = new StateProperty<>(Boolean.class),
                PERSISTENT = new StateProperty<>(Boolean.class),
                POWERED = new StateProperty<>(Boolean.class),
                SHORT = new StateProperty<>(Boolean.class),
                SIGNAL_FIRE = new StateProperty<>(Boolean.class),
                SNOWY = new StateProperty<>(Boolean.class),
                TRIGGERED = new StateProperty<>(Boolean.class),
                UNSTABLE = new StateProperty<>(Boolean.class),
                WATERLOGGED = new StateProperty<>(Boolean.class),
                VINE_END = new StateProperty<>(Boolean.class),
                UP = new StateProperty<>(Boolean.class),
                DOWN = new StateProperty<>(Boolean.class),
                NORTH = new StateProperty<>(Boolean.class),
                EAST = new StateProperty<>(Boolean.class),
                SOUTH = new StateProperty<>(Boolean.class),
                WEST = new StateProperty<>(Boolean.class);

        public static final StateProperty<Integer>
                AGE_1 = new StateProperty<>(Integer.class),
                AGE_2 = new StateProperty<>(Integer.class),
                AGE_3 = new StateProperty<>(Integer.class),
                AGE_5 = new StateProperty<>(Integer.class),
                AGE_7 = new StateProperty<>(Integer.class),
                AGE_15 = new StateProperty<>(Integer.class),
                AGE_25 = new StateProperty<>(Integer.class),
                BITES = new StateProperty<>(Integer.class),
                DELAY = new StateProperty<>(Integer.class),
                DISTANCE = new StateProperty<>(Integer.class),
                EGGS = new StateProperty<>(Integer.class),
                HATCH = new StateProperty<>(Integer.class),
                LAYERS = new StateProperty<>(Integer.class),
                LEVEL_CAULDRON = new StateProperty<>(Integer.class),
                LEVEL_COMPOSTER = new StateProperty<>(Integer.class),
                LEVEL_FLOWING = new StateProperty<>(Integer.class),
                LEVEL_HONEY = new StateProperty<>(Integer.class),
                LEVEL = new StateProperty<>(Integer.class),
                MOISTURE = new StateProperty<>(Integer.class),
                NOTE = new StateProperty<>(Integer.class),
                PICKLES = new StateProperty<>(Integer.class),
                POWER = new StateProperty<>(Integer.class),
                STAGE = new StateProperty<>(Integer.class),
                STABILITY_DISTANCE = new StateProperty<>(Integer.class),
                RESPAWN_ANCHOR_CHARGES = new StateProperty<>(Integer.class),
                ROTATION_16 = new StateProperty<>(Integer.class);

        public static final StateProperty<Axis>
                HORIZONTAL_AXIS = new StateProperty<>(Axis.class, Axis::isHorizontal),
                AXIS = new StateProperty<>(Axis.class);

        public static final StateProperty<Direction>
                FACING = new StateProperty<>(Direction.class),
                FACING_HOPPER = new StateProperty<>(Direction.class, direction -> direction != Direction.UP),
                HORIZONTAL_FACING = new StateProperty<>(Direction.class, Direction::isHorizontal);

        public static final StateProperty<FrontAndTop> ORIENTATION = new StateProperty<>(FrontAndTop.class);
        public static final StateProperty<AttachFace> ATTACH_FACE = new StateProperty<>(AttachFace.class);
        public static final StateProperty<BellAttachType> BELL_ATTACHMENT = new StateProperty<>(BellAttachType.class);

        public static final StateProperty<WallSide>
                EAST_WALL = new StateProperty<>(WallSide.class),
                NORTH_WALL = new StateProperty<>(WallSide.class),
                SOUTH_WALL = new StateProperty<>(WallSide.class),
                WEST_WALL = new StateProperty<>(WallSide.class);

        public static final StateProperty<RedstoneSide>
                EAST_REDSTONE = new StateProperty<>(RedstoneSide.class),
                NORTH_REDSTONE = new StateProperty<>(RedstoneSide.class),
                SOUTH_REDSTONE = new StateProperty<>(RedstoneSide.class),
                WEST_REDSTONE = new StateProperty<>(RedstoneSide.class);

        public static final StateProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF = new StateProperty<>(DoubleBlockHalf.class);
        public static final StateProperty<Half> HALF = new StateProperty<>(Half.class);

        public static final StateProperty<RailShape>
                RAIL_SHAPE = new StateProperty<>(RailShape.class),
                RAIL_SHAPE_STRAIGHT = new StateProperty<>(RailShape.class);

        public static final StateProperty<BedPart> BED_PART = new StateProperty<>(BedPart.class);
        public static final StateProperty<ChestType> CHEST_TYPE = new StateProperty<>(ChestType.class);
        public static final StateProperty<ComparatorMode> MODE_COMPARATOR = new StateProperty<>(ComparatorMode.class);
        public static final StateProperty<DoorHingeSide> DOOR_HINGE = new StateProperty<>(DoorHingeSide.class);
        public static final StateProperty<NoteBlockInstrument> NOTEBLOCK_INSTRUMENT = new StateProperty<>(NoteBlockInstrument.class);
        public static final StateProperty<PistonType> PISTON_TYPE = new StateProperty<>(PistonType.class);
        public static final StateProperty<SlabType> SLAB_TYPE = new StateProperty<>(SlabType.class);
        public static final StateProperty<StairsShape> STAIRS_SHAPE = new StateProperty<>(StairsShape.class);
        public static final StateProperty<StructureMode> STRUCTUREBLOCK_MODE = new StateProperty<>(StructureMode.class);
        public static final StateProperty<BambooLeaves> BAMBOO_LEAVES = new StateProperty<>(BambooLeaves.class);

        private final Class<T> valueType;
        private final Predicate<T> allowedCheck;

        StateProperty(Class<T> valueType) {
            this(valueType, value -> true);
        }

        StateProperty(Class<T> valueType, Predicate<T> allowedCheck) {
            this.valueType = valueType;
            this.allowedCheck = allowedCheck;
        }

        public Class<?> getValueType() {
            return valueType;
        }

        public boolean isAllowed(T value) {
            return allowedCheck.test(value);
        }

        public interface StatePropertyEnum extends ObjectMapped {
        }

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
