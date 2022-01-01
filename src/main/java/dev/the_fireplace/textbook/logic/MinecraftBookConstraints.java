package dev.the_fireplace.textbook.logic;

import dev.the_fireplace.textbook.domain.config.ConfigValues;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

@Environment(EnvType.CLIENT)
public final class MinecraftBookConstraints
{
    private final ConfigValues configValues;

    @Inject
    public MinecraftBookConstraints(ConfigValues configValues) {
        this.configValues = configValues;
    }

    public boolean fitsOnPage(String string) {
        boolean meetsMinecraftPageConstraints = string.length() < 1024 && MinecraftClient.getInstance().textRenderer.getStringBoundedHeight(string, 114) <= 128;
        boolean meetsCustomConstraints = configValues.getPageByteLimit() == 0 || string.getBytes(StandardCharsets.UTF_8).length <= configValues.getPageByteLimit();
        return meetsMinecraftPageConstraints && meetsCustomConstraints;
    }
}
