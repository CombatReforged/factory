package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.items.Item;
import com.combatreforged.factory.api.world.util.Location;

public interface Block {
    Location getLocation();
    World getWorld();
    Item getItem();
    float getBreakingProgress();
}
