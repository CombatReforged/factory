package com.combatreforged.factory.builder.implementation.world.entity.player;

import com.combatreforged.factory.api.world.entity.player.GameModeType;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.item.container.PlayerInventory;
import com.combatreforged.factory.api.world.util.Vector3D;
import com.combatreforged.factory.builder.extension.FoodDataExtension;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.util.Conversion;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedLivingEntity;
import com.combatreforged.factory.builder.implementation.world.item.container.WrappedPlayerInventory;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;

public class WrappedPlayer extends WrappedLivingEntity implements Player {
    final ServerPlayer wrappedPlayer;
    public WrappedPlayer(ServerPlayer wrappedPlayer) {
        super(wrappedPlayer);
        this.wrappedPlayer = wrappedPlayer;
    }

    @Override
    public int getFoodLevel() {
        return wrappedPlayer.getFoodData().getFoodLevel();
    }

    @Override
    public float getSaturation() {
        return wrappedPlayer.getFoodData().getSaturationLevel();
    }

    @Override
    public void setFoodLevel(int level) {
        wrappedPlayer.getFoodData().setFoodLevel(level);
    }

    @Override
    public void setSaturation(float saturation) {
        wrappedPlayer.getFoodData().setSaturation(saturation);
    }

    @Override
    public float getExhaustion() {
        return ((FoodDataExtension) wrappedPlayer.getFoodData()).getExhaustion();
    }

    @Override
    public void setExhaustion(float exhaustion) {
        ((FoodDataExtension) wrappedPlayer.getFoodData()).setExhaustion(exhaustion);
    }

    @Override
    public PlayerInventory getInventory() {
        return Wrapped.wrap(wrappedPlayer.inventory, WrappedPlayerInventory.class);
    }

    @Override
    public int getSelectedSlot() {
        return wrappedPlayer.inventory.selected;
    }

    @Override
    public GameModeType getGameMode() {
        return Conversion.GAME_MODES.inverse().get(wrappedPlayer.gameMode.getGameModeForPlayer());
    }

    @Override
    public void setGameMode(GameModeType gameMode) {
        wrappedPlayer.setGameMode(Conversion.GAME_MODES.get(gameMode));
    }

    @Override
    public void setVelocity(Vector3D velocity) {
        super.setVelocity(velocity);
        wrappedPlayer.connection.send(new ClientboundSetEntityMotionPacket(wrappedPlayer));
    }
}
