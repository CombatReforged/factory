package com.combatreforged.factory.api.event.player;

import com.combatreforged.factory.api.event.EventBackend;
import com.combatreforged.factory.api.world.entity.player.GameModeType;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;
import org.jetbrains.annotations.Nullable;

public class PlayerRespawnEvent extends PlayerEvent {
    public static final EventBackend<PlayerRespawnEvent> BACKEND = EventBackend.create(PlayerRespawnEvent.class);

    @Nullable private Location spawnpoint;
    private boolean spawnpointForced;
    private GameModeType respawnMode;

    public PlayerRespawnEvent(Player player, @Nullable Location spawnpoint, boolean spawnpointForced, GameModeType respawnMode) {
        super(player);
        this.spawnpoint = spawnpoint;
        this.spawnpointForced = spawnpointForced;
        this.respawnMode = respawnMode;
    }

    @Nullable
    public Location getSpawnpoint() {
        return spawnpoint;
    }

    public void setSpawnpoint(@Nullable Location spawnpoint) {
        this.spawnpoint = spawnpoint;
    }

    public boolean isSpawnpointForced() {
        return spawnpointForced;
    }

    public void setSpawnpointForced(boolean spawnpointForced) {
        this.spawnpointForced = spawnpointForced;
    }

    public GameModeType getRespawnMode() {
        return respawnMode;
    }

    public void setRespawnMode(GameModeType respawnMode) {
        this.respawnMode = respawnMode;
    }
}
