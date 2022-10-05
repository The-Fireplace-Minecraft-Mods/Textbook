package dev.the_fireplace.textbook.mixin;

import dev.the_fireplace.textbook.TextbookConstants;
import dev.the_fireplace.textbook.usecase.ExportBook;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookViewScreen.class)
public abstract class BookScreenMixin extends Screen
{
    protected BookScreenMixin(Component title) {
        super(title);
    }

    @Shadow
    private BookViewScreen.BookAccess bookAccess;

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        Checkbox preserveWhitespaceCheckbox = new Checkbox(this.width / 2 + 2, 196 + 40 + 4, 98, 20, Component.translatable("gui.textbook.export.preserve_whitespace"), true);
        this.addRenderableWidget(preserveWhitespaceCheckbox);
        this.addRenderableWidget(new Button(this.width / 2 + 2, 196 + 20 + 2, 98, 20, Component.translatable("gui.textbook.export"), (buttonWidget) -> {
            ExportBook exportBook = TextbookConstants.getInjector().getInstance(ExportBook.class);
            exportBook.export(bookAccess, preserveWhitespaceCheckbox.selected());
        }));
    }
}
