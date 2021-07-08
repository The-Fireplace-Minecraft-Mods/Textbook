package dev.the_fireplace.textbook;

import dev.the_fireplace.annotateddi.api.DIContainer;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Textbook implements ClientModInitializer {
	public static final String MODID = "textbook";
	private static final Logger LOGGER = LogManager.getLogger(MODID);
	private static TextbookLogic textbookLogic = null;

	public static Logger getLogger() {
		return LOGGER;
	}

	public static TextbookLogic getLogic() {
		if (textbookLogic == null) {
			textbookLogic = DIContainer.get().getInstance(TextbookLogic.class);
		}

		return textbookLogic;
	}

	@Override
	public void onInitializeClient() {

	}
}
