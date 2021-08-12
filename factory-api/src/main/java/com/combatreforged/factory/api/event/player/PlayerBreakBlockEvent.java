package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.block.Block;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.util.Location;

public class PlayerBreakBlockEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerBreakBlockEvent> BACKEND = EventBackend.create(PlayerBreakBlockEvent.class);

    private boolean cancelled;

    private final Block block;
    private final Location location;
    private final ItemStack miningStack;
    private boolean dropBlock;

    public PlayerBreakBlockEvent(Player player, Block block, Location location, ItemStack miningStack, boolean dropBlock) {
        super(player);
        this.block = block;
        this.location = location;
        this.miningStack = miningStack;
        this.dropBlock = dropBlock;
    }

    public ItemStack getMiningStack() {
        return miningStack;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public boolean isDropBlock() {
        return dropBlock;
    }

    public void setDropBlock(boolean dropBlock) {
        this.dropBlock = dropBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
