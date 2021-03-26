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

import java.util.*;
import java.util.stream.Collectors;

public class WrappedFactoryServer extends Wrapped<DedicatedServer> implements FactoryServer {
    final FactoryAPI api;
    public WrappedFactoryServer(DedicatedServer wrapped, FactoryAPI api) {
        super(wrapped);
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
    public List<Player> getPlayers() {
        return wrapped.getPlayerList().getPlayers()
                .stream()
                .map((player) -> Wrapped.wrap(player, WrappedPlayer.class))
                .collect(Collectors.toList());
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
        return worlds;
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
