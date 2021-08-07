package com.combatreforged.factory.builder.mixin.commands;

import com.combatreforged.factory.builder.command.FactoryCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void registerFactoryCommands(Commands.CommandSelection commandSelection, CallbackInfo ci) {
        FabricLoader loader = FabricLoader.getInstance();
        if (commandSelection == Commands.CommandSelection.DEDICATED && loader.getModContainer("factory-api").isPresent() && loader.getModContainer("factory-builder").isPresent()) {
            FactoryCommand.register(this.dispatcher);
        }
    }
}
