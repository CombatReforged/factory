package com.combatreforged.factory.builder.command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public final class FactoryCommand {
    private FactoryCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("factory")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                .executes(context -> {
                    FabricLoader loader = FabricLoader.getInstance();
                    assert loader.getModContainer("factory-api").isPresent() && loader.getModContainer("factory-builder").isPresent();
                    String apiVersion = loader.getModContainer("factory-api").get().getMetadata().getVersion().toString();
                    String builderVersion = loader.getModContainer("factory-builder").get().getMetadata().getVersion().toString();
                    Component component = new TextComponent("This server is running factory-builder@" + builderVersion + " implementing factory-api@" + apiVersion + ".");
                    context.getSource().sendSuccess(component, false);
                    return 0;
                })
        );
    }
}
