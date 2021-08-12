package com.combatreforged.factory.builder.mixin.commands;

import com.combatreforged.factory.api.FactoryAPI;
import com.combatreforged.factory.api.FactoryServer;
import com.combatreforged.factory.api.command.CommandSender;
import com.combatreforged.factory.api.command.CommandSourceInfo;
import com.combatreforged.factory.api.world.World;
import com.combatreforged.factory.api.world.entity.Entity;
import com.combatreforged.factory.api.world.util.Location;
import com.combatreforged.factory.builder.implementation.Wrapped;
import com.combatreforged.factory.builder.implementation.WrappedFactoryServer;
import com.combatreforged.factory.builder.implementation.world.WrappedWorld;
import com.combatreforged.factory.builder.implementation.world.entity.WrappedEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

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

    //TODO suggest commands on the client
}
