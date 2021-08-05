package com.combatreforged.factory.builder.command;

import com.combatreforged.factory.api.FactoryAPI;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public final class VersionCommand {
    public VersionCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("factory")
                .requires(commandSourceStack -> commandSourceStack.hasPermission(4))
                .executes(context -> {
                    FabricLoader loader = FabricLoader.getInstance();
                    assert loader.getModContainer("factory-builder").isPresent();
                    String apiVersion = FactoryAPI.getInstance().getVersion();
                    String builderVersion = loader.getModContainer("factory-builder").get().getMetadata().getVersion().toString();
                    Component component = new TextComponent("This server is running factory:" + apiVersion + "@factory-builder:" + builderVersion + ".");
                    context.getSource().sendSuccess(component, false);
                    return 0;
                })
        );
    }
}
