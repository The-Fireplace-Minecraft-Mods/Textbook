package dev.the_fireplace.textbook.entrypoints;

import dev.the_fireplace.lib.api.client.entrypoints.ConfigGuiEntrypoint;
import dev.the_fireplace.lib.api.client.interfaces.ConfigGuiRegistry;
import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.config.TextbookConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ConfigGui implements ConfigGuiEntrypoint
{
    private final TextbookConfigScreenFactory textbookConfigScreenFactory = TextbookConstants.getInjector().getInstance(TextbookConfigScreenFactory.class);

    @Override
    public void registerConfigGuis(ConfigGuiRegistry configGuiRegistry) {
        configGuiRegistry.register(TextbookConstants.MODID, textbookConfigScreenFactory::getConfigScreen);
    }
}
