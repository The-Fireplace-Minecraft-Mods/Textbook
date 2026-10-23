package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.config.TextbookConfig;
import net.minecraft.client.Minecraft;

/**
 * Replicates the original {@code dev.the_fireplace.textbook.logic.MinecraftBookConstraints}.
 *
 * <p>Minecraft itself caps a writable-book page at 1024 chars and a wrapped height of
 * 128 pixels at the standard text width of 114 pixels. The optional {@code pageByteLimit}
 * config adds a server-style UTF-8 byte cap on top of that, used by servers like Paper.</p>
 */
public final class MinecraftBookConstraints {

    /**
     * Vanilla MC book page constraints (see {@code BookEditScreen} constants):
     * {@code TEXT_WIDTH = 114}, {@code TEXT_HEIGHT = 128}.
     */
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_HEIGHT = 128;
    private static final int MAX_PAGE_CHARS = 1024;

    public boolean fitsOnPage(String string) {
        boolean meetsMinecraftPageConstraints =
                string.length() < MAX_PAGE_CHARS
                        && Minecraft.getInstance().font.wordWrapHeight(string, TEXT_WIDTH) <= TEXT_HEIGHT;
        boolean meetsCustomConstraints =
                TextbookConfig.getPageByteLimit() == 0
                        || string.getBytes(java.nio.charset.StandardCharsets.UTF_8).length
                                <= TextbookConfig.getPageByteLimit();
        return meetsMinecraftPageConstraints && meetsCustomConstraints;
    }
}
