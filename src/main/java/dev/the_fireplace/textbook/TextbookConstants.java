package dev.the_fireplace.textbook;

import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TextbookConstants
{
    public static final String MODID = "textbook";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return LOGGER;
    }

    private static Translator translator = null;

    public static Translator getTranslator() {
        if (translator == null) {
            translator = DIContainer.get().getInstance(TranslatorFactory.class).getTranslator(MODID);
        }

        return translator;
    }
}
