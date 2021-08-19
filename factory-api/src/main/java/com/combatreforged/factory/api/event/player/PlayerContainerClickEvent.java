package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.Cancellable;
import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.ItemStack;
import com.combatreforged.factory.api.world.item.container.menu.Button;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.SlotClickType;
import org.jetbrains.annotations.Nullable;

public class PlayerContainerClickEvent extends PlayerEvent implements Cancellable {
    public static final EventBackend<PlayerContainerClickEvent> BACKEND = EventBackend.create(PlayerContainerClickEvent.class);

    private boolean cancelled;

    private final ContainerMenu menu;
    private final int targetSlot; // -1 for Button.DROP, -999 if no slot is selected (depends on type)
    private final SlotClickType slotClickType;
    private final Button button;
    @Nullable private final ItemStack targetStack;
    @Nullable private final ItemStack cursorStack;

    public PlayerContainerClickEvent(Player player, ContainerMenu menu, int targetSlot, SlotClickType slotClickType, Button button, @Nullable ItemStack targetStack, @Nullable ItemStack cursorStack) {
        super(player);
        this.menu = menu;
        this.targetSlot = targetSlot;
        this.slotClickType = slotClickType;
        this.button = button;
        this.targetStack = targetStack;
        this.cursorStack = cursorStack;
    }

    public ContainerMenu getMenu() {
        return menu;
    }

    public int getTargetSlot() {
        return targetSlot;
    }

    public SlotClickType getClickType() {
        return slotClickType;
    }

    public Button getButton() {
        return button;
    }

    @Nullable public ItemStack getTargetStack() {
        return targetStack;
    }

    @Nullable public ItemStack getCursorStack() {
        return cursorStack;
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
