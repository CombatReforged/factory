package com.combatreforged.factory.api.command;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class CommandSourceInfo {
    private final CommandSender sender;
    @Nullable private final Entity executingEntity;
    private final Location location;
    private final FactoryServer server;

    private CommandSourceInfo(CommandSender sender, @Nullable Entity executingEntity, Location location, FactoryServer server) {
        this.sender = sender;
        this.executingEntity = executingEntity;
        this.location = location;
        this.server = server;
    }

    public CommandSender getSender() {
        return sender;
    }

    @Nullable public Entity getExecutingEntity() {
        return executingEntity;
    }

    public Location getLocation() {
        return location;
    }

    public FactoryServer getServer() {
        return server;
    }

    public void sendMessage(Component message) {
        this.getSender().sendMessage(message);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CommandSender source;
        @Nullable private Entity executingEntity;
        private Location location;
        private FactoryServer server;

        Builder() {}

        public Builder source(CommandSender source) {
            this.source = source;
            return this;
        }

        public Builder executingEntity(Entity entity) {
            this.executingEntity = entity;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Builder server(FactoryServer server) {
            this.server = server;
            return this;
        }

        public CommandSourceInfo build() {
            return new CommandSourceInfo(this.source, this.executingEntity, this.location, this.server);
        }
    }
}
