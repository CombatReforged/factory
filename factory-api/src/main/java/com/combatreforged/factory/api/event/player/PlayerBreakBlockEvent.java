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
    private final World world;
    private final ItemStack miningStack;
    private boolean drop;

    public PlayerBreakBlockEvent(Player player, Block block, Location location, World world, ItemStack miningStack, boolean drop) {
        super(player);
        this.block = block;
        this.location = location;
        this.world = world;
        this.miningStack = miningStack;
        this.drop = drop;
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
        return world;
    }

    public boolean isDrop() {
        return drop;
    }

    public void setDrop(boolean drop) {
        this.drop = drop;
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
