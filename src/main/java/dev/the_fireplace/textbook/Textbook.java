package dev.the_fireplace.textbook;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Textbook implements ClientModInitializer {
	public static final String MODID = "textbook";
	private static final Logger LOGGER = LogManager.getLogger(MODID);

	public static Logger getLogger() {
		return LOGGER;
	}

	@Override
	public void onInitializeClient() {

	}
}
