package com.combatreforged.factory.builder.command;

import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.Component;

import static com.combatreforged.factory.api.command.CommandUtils.literal;

public final class FactoryCommand {
    private FactoryCommand() {}

    public static void register(CommandDispatcher<CommandSourceInfo> dispatcher) {
        dispatcher.register(
                literal("factory")
                        .requires(info -> info.getSender().getPermissionLevel() >= 4)
                        .executes(context -> {
                            FabricLoader loader = FabricLoader.getInstance();
                            assert loader.getModContainer("factory-api").isPresent() && loader.getModContainer("factory-builder").isPresent();
                            String apiVersion = loader.getModContainer("factory-api").get().getMetadata().getVersion().toString();
                            String builderVersion = loader.getModContainer("factory-builder").get().getMetadata().getVersion().toString();
                            context.getSource().sendMessage(Component.text("This server is running factory-builder@" + builderVersion + " implementing factory-api@" + apiVersion + "."));
                            return 0;
                        })
        );
    }
}
