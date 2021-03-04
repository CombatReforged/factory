package com.combatreforged.factory.builder.implementation;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.util.Identifier;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.dedicated.DedicatedServer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WrappedFactoryServer extends Wrapped<DedicatedServer> implements FactoryServer {
    final DedicatedServer wrapped;
    final FactoryAPI api;
    public WrappedFactoryServer(DedicatedServer wrapped, FactoryAPI api) {
        super(wrapped);
        this.wrapped = wrapped;
        this.api = api;
    }


    @Override
    public FactoryAPI getAPI() {
        return api;
    }

    @Override
    public int getMaxPlayerCount() {
        return wrapped.getMaxPlayers();
    }

    @Override
    public Collection<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        wrapped.getPlayerList().getPlayers().forEach(player -> players.add(Wrapped.wrap(player, WrappedPlayer.class)));
        return players; //TODO Improve efficiency
    }

    @Override
    public Player getPlayer(String name) {
        return Wrapped.wrap(wrapped.getPlayerList().getPlayerByName(name), WrappedPlayer.class);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return Wrapped.wrap(wrapped.getPlayerList().getPlayer(uuid), WrappedPlayer.class);
    }

    @Override
    public Collection<World> getWorlds() {
        Set<World> worlds = new HashSet<>();
        wrapped.getAllLevels().forEach((world) -> {
            worlds.add(Wrapped.wrap(world, WrappedWorld.class));
        });
        return worlds; //TODO Improve efficiency
    }

    @Override
    public World getWorld(Identifier identifier) {
        return Wrapped.wrap(wrapped.getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY,
                        new ResourceLocation(identifier.getNamespace(), identifier.getId()))), WrappedWorld.class);
    }

    @Override
    public int runCommand(String command) {
        return wrapped.getCommands().performCommand(wrapped.createCommandSourceStack(), command);
    }
}
