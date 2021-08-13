package com.combatreforged.factory.builder.command;

import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;
import java.util.stream.Collectors;

import static com.combatreforged.factory.api.command.CommandUtils.literal;

public final class FactoryCommand {
    private FactoryCommand() {}

    public static void register(CommandDispatcher<CommandSourceInfo> dispatcher) {
        dispatcher.register(
                literal("factory")
                        .requires(info -> info.getSender().getPermissionLevel() >= 4)
                        .then(literal("version")
                                .executes(context -> {
                                    FabricLoader loader = FabricLoader.getInstance();
                                    assert loader.getModContainer("factory-api").isPresent() && loader.getModContainer("factory-builder").isPresent();
                                    String apiVersion = loader.getModContainer("factory-api").get().getMetadata().getVersion().toString();
                                    String builderVersion = loader.getModContainer("factory-builder").get().getMetadata().getVersion().toString();
                                    context.getSource().sendMessage(Component.text("Factory version: \n ")
                                            .append(Component.text("- API: [factory-api@" + apiVersion + "]\n - Implementation: [factory-builder@" + builderVersion + "]")
                                                    .color(NamedTextColor.DARK_GREEN)));
                                    return 0;
                                }))
                        .then(literal("plugins")
                                .executes(context -> {
                                    List<ModMetadata> plugins = FabricLoader.getInstance().getAllMods()
                                            .stream()
                                            .map(ModContainer::getMetadata)
                                            .filter(metadata -> metadata.getDepends().stream()
                                                    .anyMatch(dep -> dep.getModId().equals("factory-api")))
                                            .collect(Collectors.toList());
                                    Component result = Component.text("Found " + plugins.size() + " plugin(s): ");
                                    for (ModMetadata plugin : plugins) {
                                        result = result.append(Component.text("[" + plugin.getId() + "@" + plugin.getVersion().toString() + "]"));
                                        if (plugins.indexOf(plugin) != plugins.size() - 1) {
                                            result = result.append(Component.text(", "));
                                        }
                                    }
                                    context.getSource().sendMessage(result);
                                    return 0;
                                }))
        );
    }
}
