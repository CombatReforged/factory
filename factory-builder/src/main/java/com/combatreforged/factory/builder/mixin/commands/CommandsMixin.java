package com.combatreforged.factory.builder.mixin.commands;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.command.CommandSender;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.entity.player.Player;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.combatreforged.factory.builder.implementation.world.entity.player.WrappedPlayer;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

    // Makes non-syntax errors log
    @Redirect(method = "performCommand", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;isDebugEnabled()Z",remap = false))
    public boolean activateCommandExecutionErrors(Logger instance) {
        return true;
    }

    @Redirect(method = "performCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I", remap = false))
    public int enableAPICommands(CommandDispatcher<?> commandDispatcher, StringReader input, Object source, CommandSourceStack commandSourceStack, String string) throws CommandSyntaxException {
        if (commandDispatcher == this.dispatcher) {
            CommandDispatcher<CommandSourceInfo> apiDispatcher = FactoryAPI.getInstance().getServer().getCommandDispatcher();
            CommandSource source1 = ((CommandSourceStackAccessor) commandSourceStack).getSource();
            CommandSender sender = source1 instanceof MinecraftServer ?
                    Wrapped.wrap(source1, WrappedFactoryServer.class)
                    : Wrapped.wrap(source1, WrappedEntity.class);
            Entity entity = Wrapped.wrap(commandSourceStack.getEntity(), WrappedEntity.class);
            FactoryServer server = Wrapped.wrap(commandSourceStack.getServer(), WrappedFactoryServer.class);
            Vec3 vec3 = commandSourceStack.getPosition();
            Vec2 vec2 = commandSourceStack.getRotation();
            World world = Wrapped.wrap(commandSourceStack.getLevel(), WrappedWorld.class);
            Location loc = new Location(vec3.x, vec3.y, vec3.z, vec2.y, vec2.x, world);
            CommandSourceInfo info = CommandSourceInfo.builder()
                    .source(sender)
                    .executingEntity(entity)
                    .server(server)
                    .location(loc)
                    .build();
            try {
                return apiDispatcher.execute(input, info);
            } catch (CommandSyntaxException e) {
                return this.dispatcher.execute(input, commandSourceStack);
            }
        } else {
            return dispatcher.execute(input, (CommandSourceStack) source);
        }
    }

    @Inject(method = "sendCommands", at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;fillUsableCommands(Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/tree/CommandNode;Lnet/minecraft/commands/CommandSourceStack;Ljava/util/Map;)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    public void injectAPICommands(ServerPlayer serverPlayer, CallbackInfo ci, Map<CommandNode<CommandSourceStack>, CommandNode<SharedSuggestionProvider>> map, RootCommandNode<SharedSuggestionProvider> rootCommandNode) {
        CommandDispatcher<CommandSourceInfo> apiDispatcher = FactoryAPI.getInstance().getServer().getCommandDispatcher();

        Map<CommandNode<CommandSourceInfo>, CommandNode<SharedSuggestionProvider>> apiMap = new HashMap<>();
        apiMap.put(apiDispatcher.getRoot(), rootCommandNode);

        Player player = Wrapped.wrap(serverPlayer, WrappedPlayer.class);
        this.fillAPICommands(apiDispatcher.getRoot(), rootCommandNode, player.createCommandInfo(), apiMap);
    }

    // Copy of Commands.fillUsableCommands(...) for our dispatcher
    // Bit sketchy, but eh...
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void fillAPICommands(CommandNode<CommandSourceInfo> commandNode, CommandNode<SharedSuggestionProvider> commandNode2, CommandSourceInfo commandSourceStack, Map<CommandNode<CommandSourceInfo>, CommandNode<SharedSuggestionProvider>> map) {
        for (CommandNode<CommandSourceInfo> commandSourceInfoCommandNode : commandNode.getChildren()) {
            if (commandSourceInfoCommandNode.canUse(commandSourceStack)) {
                ArgumentBuilder<SharedSuggestionProvider, ?> argumentBuilder = (ArgumentBuilder) commandSourceInfoCommandNode.createBuilder();
                argumentBuilder.requires((sharedSuggestionProvider) -> true);
                if (argumentBuilder.getCommand() != null) {
                    argumentBuilder.executes((commandContext) -> 0);
                }

                if (argumentBuilder instanceof RequiredArgumentBuilder) {
                    RequiredArgumentBuilder<SharedSuggestionProvider, ?> requiredArgumentBuilder = (RequiredArgumentBuilder) argumentBuilder;
                    if (requiredArgumentBuilder.getSuggestionsProvider() != null) {
                        requiredArgumentBuilder.suggests(SuggestionProviders.safelySwap(requiredArgumentBuilder.getSuggestionsProvider()));
                    }
                }

                if (argumentBuilder.getRedirect() != null) {
                    argumentBuilder.redirect(map.get(argumentBuilder.getRedirect()));
                }

                CommandNode<SharedSuggestionProvider> commandNode4 = argumentBuilder.build();
                map.put(commandSourceInfoCommandNode, commandNode4);
                commandNode2.addChild(commandNode4);
                if (!commandSourceInfoCommandNode.getChildren().isEmpty()) {
                    this.fillAPICommands(commandSourceInfoCommandNode, commandNode4, commandSourceStack, map);
                }
            }
        }

    }
}
