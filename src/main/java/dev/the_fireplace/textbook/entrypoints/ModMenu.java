package dev.the_fireplace.textbook.entrypoints;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.config.TextbookConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi
{
    private final TextbookConfigScreenFactory textbookConfigScreenFactory = TextbookConstants.getInjector().getInstance(TextbookConfigScreenFactory.class);

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return textbookConfigScreenFactory::getConfigScreen;
    }
}
