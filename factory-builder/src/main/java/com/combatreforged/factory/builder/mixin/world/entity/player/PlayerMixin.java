package com.combatreforged.factory.builder.mixin.world.entity.player;

import com.combatreforged.factory.builder.extension.world.entity.LivingEntityExtension;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin implements LivingEntityExtension {
    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropEquipment(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_KEEPINVENTORY) && this.getDeathEvent() != null) {
            return !getDeathEvent().isDropEquipment();
        } else {
            return gameRules.getBoolean(key);
        }
    }

    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    public boolean changeShouldDropExperience(GameRules gameRules, GameRules.Key<GameRules.BooleanValue> key) {
        if (key.equals(GameRules.RULE_KEEPINVENTORY) && this.getDeathEvent() != null) {
            return !this.getDeathEvent().isDropExperience();
        } else {
            return gameRules.getBoolean(key);
        }
    }
}
