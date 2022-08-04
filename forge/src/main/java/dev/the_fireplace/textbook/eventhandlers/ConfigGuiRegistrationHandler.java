package dev.the_fireplace.textbook.eventhandlers;

import dev.the_fireplace.lib.api.events.ConfigScreenRegistration;
import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.config.TextbookConfigScreenFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.inject.Inject;

public final class ConfigGuiRegistrationHandler
{
    private final TextbookConfigScreenFactory configScreenFactory;

    @Inject
    public ConfigGuiRegistrationHandler(TextbookConfigScreenFactory configScreenFactory) {
        this.configScreenFactory = configScreenFactory;
    }

    @SubscribeEvent
    public void registerConfigGui(ConfigScreenRegistration configScreenRegistration) {
        configScreenRegistration.getConfigGuiRegistry().register(TextbookConstants.MODID, configScreenFactory::getConfigScreen);
    }
}