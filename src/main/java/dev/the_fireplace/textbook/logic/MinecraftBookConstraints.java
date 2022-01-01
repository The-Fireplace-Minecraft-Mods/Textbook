package dev.the_fireplace.textbook.logic;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class MinecraftBookConstraints
{
    public boolean fitsOnPage(String string) {
        return string.length() < 1024 && MinecraftClient.getInstance().textRenderer.getWrappedLinesHeight(string, 114) <= 128;
    }
}
