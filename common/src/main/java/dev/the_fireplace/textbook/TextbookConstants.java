package dev.the_fireplace.textbook;

import com.google.inject.Injector;
import dev.the_fireplace.annotateddi.api.Injectors;
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

    public static Injector getInjector() {
        return Injectors.INSTANCE.getAutoInjector(MODID);
    }

    private static Translator translator = null;

    public static Translator getTranslator() {
        if (translator == null) {
            translator = getInjector().getInstance(TranslatorFactory.class).getTranslator(MODID);
        }

        return translator;
    }
}
