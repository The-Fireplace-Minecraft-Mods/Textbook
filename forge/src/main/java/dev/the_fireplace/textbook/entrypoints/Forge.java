package dev.the_fireplace.textbook.entrypoints;

import com.google.inject.Injector;
import dev.the_fireplace.lib.api.events.FLEventBus;
import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.eventhandlers.ConfigGuiRegistrationHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmllegacy.network.FMLNetworkConstants;

@Mod("textbook")
public final class Forge
{
    public Forge() {
        Injector injector = TextbookConstants.getInjector();
        DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            FLEventBus.BUS.register(injector.getInstance(ConfigGuiRegistrationHandler.class));
            return null;
        });

        // Register as optional on both sides
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
