package com.combatreforged.factory.builder.mixin.world.level.block;

import com.combatreforged.factory.api.event.player.PlayerBreakBlockEvent;
import com.combatreforged.factory.builder.extension.world.level.BlockExtension;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Block.class)
public abstract class BlockMixin implements BlockExtension {
    private static PlayerBreakBlockEvent currentBreakBlockEvent;

    @Override
    public void currentBreakEvent(PlayerBreakBlockEvent playerBreakBlockEvent) {
        currentBreakBlockEvent = playerBreakBlockEvent;
    }

    @SuppressWarnings("unused")
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean shouldDropBlock(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_DOBLOCKDROPS) && currentBreakBlockEvent != null) {
            return currentBreakBlockEvent.isDropBlock();
        } else {
            return gameRules.getBoolean(key);
        }
    }
}
