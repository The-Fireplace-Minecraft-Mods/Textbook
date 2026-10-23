package dev.the_fireplace.textbook.mixin;

import dev.the_fireplace.textbook.logic.FileExporter;
import dev.the_fireplace.textbook.logic.MinecraftBookConstraints;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
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

/**
 * Adds an "Export to file" button and a "Preserve whitespace" checkbox to
 * {@link BookViewScreen} (the read-only book screen used for both
 * {@code WrittenBookScreen} and the lectern view).
 *
 * <p>Layout matches the original Fabric version:
 * <ul>
 *   <li>"Export to file" button at {@code width/2 + 2, 218}, width 98.</li>
 *   <li>"Preserve whitespace" checkbox at {@code width/2 + 2, 240}.</li>
 * </ul>
 * </p>
 *
 * <p><b>Mixin note:</b> The Mixin runtime never invokes the mixin's own
 * constructor — it only merges bytecode into the target class. Initialising
 * instance fields in the constructor would leave them {@code null} at runtime.
 * We therefore initialise helpers inline at the field declaration.</p>
 */
@Mixin(BookViewScreen.class)
public abstract class BookScreenMixin extends Screen {

    @Shadow
    private BookViewScreen.BookAccess bookAccess;

    // Stateless helpers — safe to construct once per instance, initialised inline
    // (Mixins don't call our constructor at runtime, so we can't init in it).
    private final MinecraftBookConstraints textbookConstraints = new MinecraftBookConstraints();
    private final FileExporter textbookFileExporter = new FileExporter(textbookConstraints);
    private final TextbookFileDialogs textbookDialogs = new TextbookFileDialogs();
    private final ExportBook exportBook = new ExportBook(textbookDialogs, textbookFileExporter);

    protected BookScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void textbook$init(CallbackInfo info) {
        Checkbox preserveWhitespaceCheckbox =
                Checkbox.builder(
                                Component.translatable("gui.textbook.export.preserve_whitespace"),
                                this.font
                        )
                        .pos(this.width / 2 + 2, 240)
                        .build();
        this.addRenderableWidget(preserveWhitespaceCheckbox);
        this.addRenderableWidget(
                Button.builder(Component.translatable("gui.textbook.export"), b ->
                        this.exportBook.export(this.bookAccess, preserveWhitespaceCheckbox.selected()))
                        .pos(this.width / 2 + 2, 218)
                        .width(98)
                        .build()
        );
    }
}
