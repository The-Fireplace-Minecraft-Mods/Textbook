package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.domain.config.ConfigValues;
import net.minecraft.client.Minecraft;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

public final class MinecraftBookConstraints
{
    private final ConfigValues configValues;

    @Inject
    public MinecraftBookConstraints(ConfigValues configValues) {
        this.configValues = configValues;
    }

    public boolean fitsOnPage(String string) {
        boolean meetsMinecraftPageConstraints = string.length() < 1024 && Minecraft.getInstance().font.wordWrapHeight(string, 114) <= 128;
        boolean meetsCustomConstraints = configValues.getPageByteLimit() == 0 || string.getBytes(StandardCharsets.UTF_8).length <= configValues.getPageByteLimit();
        return meetsMinecraftPageConstraints && meetsCustomConstraints;
    }
}
