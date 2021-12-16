package com.combatreforged.factory.builder.mixin;

import net.minecraft.server.Main;
import net.minecrell.terminalconsole.TerminalConsoleAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MainMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void changeLogger(String[] strings, CallbackInfo ci) {
        /*LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        PatternLayout layout = PatternLayout.newBuilder().withPattern("[%d{HH:mm:ss.SSS}] [%logger{1}/%level] %msg%n").build();
        Appender appender = TerminalConsoleAppender.createAppender("Console", null, layout, false);
        appender.start();
        config.addAppender(appender);
        ctx.updateLoggers();*/
    }
}
