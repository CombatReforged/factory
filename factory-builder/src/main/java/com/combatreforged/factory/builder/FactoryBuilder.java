package com.combatreforged.factory.builder;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FactoryBuilder implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Factory Builder");
	public static final String PREFIX = "[Factory Builder] ";

	@Override
	public void onInitialize() {
		LOGGER.info(PREFIX + "Building the Factory API...");
	}
}
