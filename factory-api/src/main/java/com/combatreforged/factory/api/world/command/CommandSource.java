package com.combatreforged.factory.api.world.command;

public interface CommandSource {
    default int runCommand(String command) {
        return runCommand(command, getPermissionLevel(), true);
    }

    default int runCommand(String command, int permissionLevel) {
        return runCommand(command, permissionLevel, true);
    }

    int runCommand(String command, int permissionLevel, boolean giveFeedback);

    int getPermissionLevel();
}
