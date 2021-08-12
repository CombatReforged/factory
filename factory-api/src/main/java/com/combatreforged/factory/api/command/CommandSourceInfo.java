package com.combatreforged.factory.api.command;

import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface CommandSourceInfo {
    CommandSender getSender();

    @Nullable Entity getExecutingEntity();

    Location getLocation();

    FactoryServer getServer();

    void sendMessage(Component message);

    static Builder builder() {
        return new Builder();
    }

    class Builder {
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
            return com.combatreforged.factory.api.builder.Builder.getInstance().createCommandSourceInfo(source, executingEntity, location, server);
        }
    }
}
