package com.combatreforged.factory.api.world.entity.player;

import com.combatreforged.factory.api.builder.Builder;
import com.combatreforged.factory.api.interfaces.MessageReceiver;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.LivingEntity;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.item.container.menu.ContainerMenu;
import com.combatreforged.factory.api.world.item.container.menu.MenuHolder;
import com.combatreforged.factory.api.world.scoreboard.Scoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    boolean isFlying();
    void setFlying(boolean flying);
    boolean isAbleToFly();
    void setAbleToFly(boolean ableToFly);
    boolean isFallFlying();
    void setFallFlying(boolean fallFlying);

    void setExperienceLevel(int level);
    void setExperiencePoints(int points);
    int getExperienceLevel();
    int getExperiencePoints();

    void sendTitle(Title title);
    void clearTitle();
    void resetTitle();
    void sendActionBarMessage(Component component);

    void openMenu(MenuHolder creator);
    void closeMenu();
    @Nullable ContainerMenu getOpenMenu();

    GameModeType getGameMode();
    void setGameMode(GameModeType gameMode);

    String getRawName();
    UUID getUUID();

    Scoreboard getScoreboard();
    void setScoreboard(Scoreboard scoreboard);
    void setServerScoreboard();

    Component getDisplayName();

    void respawn();

    void showPlayerInTabList(Player player, boolean show);
    List<Player> getShownInTabList();

    static Player createNPCPlayer(World world, UUID uuid, String name) {
        return Builder.getInstance().createNPCPlayer(world, uuid, name);
    }
}
