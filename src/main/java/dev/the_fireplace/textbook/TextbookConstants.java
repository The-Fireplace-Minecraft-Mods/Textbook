package dev.the_fireplace.textbook;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TextbookConstants
{
    public static final String MODID = "textbook";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Logger getLogger() {
        return LOGGER;
    }
}
