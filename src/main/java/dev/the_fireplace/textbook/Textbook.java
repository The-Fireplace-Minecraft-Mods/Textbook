package dev.the_fireplace.textbook;

import dev.the_fireplace.textbook.config.TextbookConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Textbook.MODID)
public class Textbook {
    public static final String MODID = "textbook";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Textbook(IEventBus modEventBus, ModContainer modContainer) {
        TextbookConfig.register(modContainer);
        // Register the standard NeoForge config screen so users can edit
        // `pageByteLimit` from the Mods menu → Config button.
        modContainer.registerExtensionPoint(
                IConfigScreenFactory.class,
                ConfigurationScreen::new
        );
        LOGGER.info("Textbook loaded (NeoForge port)");
    }
}
