package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.block.BlockState;
import com.combatreforged.factory.api.world.entity.equipment.HandSlot;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.util.Location;

public class PlayerPlaceBlockEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerPlaceBlockEvent> BACKEND = EventBackend.create(PlayerPlaceBlockEvent.class);

    private boolean cancelled;

    private final Location location;
    private final Block currentBlockState;
    private BlockState newBlockState;
    private final ItemStack blockStack;
    private final HandSlot placingHand;

    public PlayerPlaceBlockEvent(Player player, Location location, Block currentBlockState, BlockState newBlockState, ItemStack blockStack, HandSlot placingHand) {
        super(player);
        this.location = location;
        this.currentBlockState = currentBlockState;
        this.newBlockState = newBlockState;
        this.blockStack = blockStack;
        this.placingHand = placingHand;
    }

    public Location getLocation() {
        return location;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public Block getCurrentBlockState() {
        return currentBlockState;
    }

    public BlockState getNewBlockState() {
        return newBlockState;
    }

    public void setNewBlockState(BlockState newBlockState) {
        this.newBlockState = newBlockState;
    }

    public ItemStack getBlockStack() {
        return blockStack;
    }

    public HandSlot getPlacingHand() {
        return placingHand;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
