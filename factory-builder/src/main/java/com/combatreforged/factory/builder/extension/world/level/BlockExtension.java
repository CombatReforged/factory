package com.combatreforged.factory.builder.extension.world.level;

import com.combatreforged.factory.api.event.player.PlayerBreakBlockEvent;

public interface BlockExtension {
    void currentBreakEvent(PlayerBreakBlockEvent playerBreakBlockEvent);
}
