package com.combatreforged.factory.api;

import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.command.CommandSource;
import com.combatreforged.factory.api.world.entity.player.Player;

import java.util.Collection;
import java.util.UUID;

public interface FactoryServer extends CommandSource {
    FactoryAPI getAPI();

    int getMaxPlayerCount();
    Collection<Player> getPlayers();
    Player getPlayer(String name);
    Player getPlayer(UUID uuid);
    Collection<World> getWorlds();
    World getWorld(Identifier identifier);
}
