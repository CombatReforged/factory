package com.combatreforged.factory.api.world.block;

import com.combatreforged.factory.api.world.item.Item;

public interface Block {
    boolean isAir();
    Item getDrop();
}
