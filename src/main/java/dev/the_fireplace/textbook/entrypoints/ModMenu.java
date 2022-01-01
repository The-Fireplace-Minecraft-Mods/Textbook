package dev.the_fireplace.textbook.entrypoints;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.the_fireplace.annotateddi.api.DIContainer;
import dev.the_fireplace.textbook.config.TextbookConfigScreenFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi
{
    private final TextbookConfigScreenFactory textbookConfigScreenFactory = DIContainer.get().getInstance(TextbookConfigScreenFactory.class);

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return textbookConfigScreenFactory::getConfigScreen;
    }
}
