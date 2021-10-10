package com.combatreforged.factory.builder.extension.server;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.scheduler.TaskScheduler;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.level.ServerPlayer;

public final class FixUtils {
    static TaskScheduler scheduler = FactoryAPI.getInstance().getScheduler();

    public static void updateDelayed(ServerPlayer player) {
        scheduler.schedule(() -> {
            player.connection.send(new ClientboundSetEntityDataPacket(player.getId(), player.getEntityData(), true));
            player.connection.send(new ClientboundUpdateAttributesPacket(player.getId(), player.getAttributes().getSyncableAttributes()));
        }, 1);
    }
}
