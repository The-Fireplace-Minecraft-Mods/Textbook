package dev.the_fireplace.textbook.api;

import dev.the_fireplace.lib.api.chat.interfaces.Translator;
import java.util.Optional;
import net.minecraft.client.gui.screen.Screen;

/**
 * Client side only.
 */
public interface ConfigScreenBuilderFactoryNew
{
    /**
     * Create a new config screen builder.
     * If no supported config GUI mods are available, returns empty.
     * Should never be empty when creating from the Config GUI entrypoint (Fabric) or event (Forge).
     */
    ConfigScreenBuilderNew create(
            Translator translator,
            String titleTranslationKey,
            String initialCategoryTranslationKey,
            Screen parent,
            Runnable save
    );
}

