package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.interfaces.MessageReceiver;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.types.Minecraft;
import net.kyori.adventure.title.Title;

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

    static Player create(World world) {
        return (Player) Builder.getInstance().createEntity(Minecraft.Entity.PLAYER, world);
    }
}
