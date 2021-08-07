package com.combatreforged.factory.api;

import com.combatreforged.factory.api.command.CommandSender;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.UUID;

public interface FactoryServer extends CommandSender {
    int getMaxPlayerCount();
    Collection<Player> getPlayers();
    Player getPlayer(String name);
    Player getPlayer(UUID uuid);
    Collection<World> getWorlds();
    World getWorld(Identifier identifier);
    Scoreboard getServerScoreboard();
}
