package dev.the_fireplace.textbook.config;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * NeoForge-side replacement for the original TextbookConfig / DefaultConfigValues / ConfigValues
 * triple from the Fabric version. The original mod used Annotated DI + Fireplace Lib's
 * Config interface to persist a single integer {@code pageByteLimit}; NeoForge ships its own
 * config system, so we re-use it directly.
 */
public final class TextbookConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue PAGE_BYTE_LIMIT = BUILDER
            .comment(
                    "Limit the number of bytes that a page can hold.",
                    "Useful on servers that enforce limits using mods/plugins such as Paper.",
                    "Set to 0 for no byte limit."
            )
            .defineInRange("pageByteLimit", 0, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec SPEC = BUILDER.build();

    private TextbookConfig() {
    }

    public static void register(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, SPEC);
    }

    public static int getPageByteLimit() {
        return PAGE_BYTE_LIMIT.get();
    }
}
