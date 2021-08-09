package com.combatreforged.factory.api.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public final class CommandUtils {
    private CommandUtils() {}

    public static LiteralArgumentBuilder<CommandSourceInfo> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }
}
