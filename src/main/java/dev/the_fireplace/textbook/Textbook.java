package dev.the_fireplace.textbook;

import dev.the_fireplace.lib.api.chat.Translator;
import dev.the_fireplace.lib.api.chat.TranslatorManager;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Textbook implements ClientModInitializer {
	public static final String MODID = "textbook";
	private static final Logger LOGGER = LogManager.getLogger(MODID);
	public static Logger getLogger() {
		return LOGGER;
	}
	private static final Translator TRANSLATOR = TranslatorManager.getInstance().getTranslator(MODID);
	public static Translator getTranslator() {
		return TRANSLATOR;
	}

	@Override
	public void onInitializeClient() {

	}
}
