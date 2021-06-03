package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.interfaces.MessageReceiver;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.types.Minecraft;
import net.kyori.adventure.title.Title;

import java.util.UUID;

public interface Player extends LivingEntity, MessageReceiver {
    int getFoodLevel();
    void setFoodLevel(int level);
    float getSaturation();
    void setSaturation(float saturation);
    float getExhaustion();
    void setExhaustion(float exhaustion);
    PlayerInventory getInventory();
    int getSelectedSlot();

    void sendTitle(Title title);

    GameModeType getGameMode();
    void setGameMode(GameModeType gameMode);

    UUID getUUID();

    static Player createNPCPlayer(World world, UUID uuid, String name) {
        return Builder.getInstance().createNPCPlayer(world, uuid, name);
    }
}
