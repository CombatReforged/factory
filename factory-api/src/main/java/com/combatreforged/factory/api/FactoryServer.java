package com.combatreforged.factory.api;

import com.combatreforged.factory.api.command.CommandSender;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import com.mojang.brigadier.CommandDispatcher;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FactoryServer extends CommandSender {
    int getMaxPlayerCount();
    Collection<Player> getPlayers();
    Player getPlayer(String name);
    Player getPlayer(UUID uuid);
    Collection<World> getWorlds();
    World getWorld(Identifier identifier);
    Scoreboard getServerScoreboard();
    CommandDispatcher<CommandSourceInfo> getCommandDispatcher();

    boolean hasWorld(String name);
    void loadWorld(Path path);
    void loadWorld(Path path, String name);
    CompletableFuture<Void> loadWorldAsync(Path path);
    CompletableFuture<Void> loadWorldAsync(Path path, String name);
    void saveWorld(String name);
    void unloadWorld(String name) throws IOException;
    void unloadWorld(String name, boolean save) throws IOException;
}
