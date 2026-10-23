package dev.the_fireplace.textbook.mixin;

import com.google.common.collect.Lists;
import dev.the_fireplace.textbook.Textbook;
import dev.the_fireplace.textbook.logic.FileImporter;
import dev.the_fireplace.textbook.logic.LineToPageConverter;
import dev.the_fireplace.textbook.logic.MinecraftBookConstraints;
import dev.the_fireplace.textbook.logic.TextbookFileDialogs;
import dev.the_fireplace.textbook.usecase.ImportBook;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Adds the "Import from file" / "Import from clipboard" buttons and the volume selector
 * to {@link BookEditScreen}.
 *
 * <p>Behaviour is the same as the original Fabric version:
 * <ul>
 *   <li>Import buttons live at y=196/218 alongside the existing Sign/Done buttons.</li>
 *   <li>The volume selector (up / down arrows + "Select volume X of Y" confirm button)
 *       is shown only when the imported text exceeds 100 pages, allowing the user to
 *       split the text across multiple books of 100 pages each.</li>
 *   <li>Selecting a volume sets the page list to that 100-page chunk and appends
 *       "-NN" (zero-padded) to the book title so consecutive volumes can be
 *       identified in the player's inventory.</li>
 * </ul>
 * </p>
 *
 * <p><b>Mixin note:</b> The Mixin runtime never invokes the mixin's own
 * constructor — it only merges bytecode into the target class. Initialising
 * instance fields in the constructor would leave them {@code null} at runtime.
 * We therefore initialise helpers inline at the field declaration.</p>
 */
@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {

    @Mutable
    @Shadow
    @Final
    private List<String> pages;

    @Shadow
    private int currentPage;

    @Shadow
    private String title;

    @Shadow
    private boolean isModified;

    @Shadow
    private boolean isSigning;

    @Shadow
    @Final
    private TextFieldHelper pageEdit;

    @Shadow
    protected abstract void clearDisplayCache();

    @Shadow
    protected abstract void clearDisplayCacheAfterPageChange();

    @Shadow
    protected abstract void updateButtonVisibility();

    private Button importButton;
    private Button importClipboardButton;
    private Button volumeConfirmButton;
    private static int selectedVolume = 1;
    private Button upArrow;
    private Button downArrow;
    private boolean textbookButtonsInitialized = false;

    // Stateless helpers — initialised inline (Mixins don't call our constructor at runtime).
    // LineToPageConverter has per-import state but the others are stateless, so we build
    // the whole ImportBook pipeline fresh inline per screen instance — cheap and safe.
    private final MinecraftBookConstraints textbookConstraints = new MinecraftBookConstraints();
    private final LineToPageConverter textbookConverter = new LineToPageConverter(textbookConstraints);
    private final FileImporter fileImporter = new FileImporter(textbookConverter);
    private final TextbookFileDialogs textbookDialogs = new TextbookFileDialogs();
    private final ImportBook importBook = new ImportBook(textbookDialogs, fileImporter);

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void textbook$init(CallbackInfo info) {
        this.importButton = (Button) this.addRenderableWidget(
                Button.builder(Component.translatable("gui.textbook.import"), b -> this.importFileText())
                        .pos(this.width / 2 + 2, 218)
                        .width(98)
                        .build()
        );
        this.importClipboardButton = (Button) this.addRenderableWidget(
                Button.builder(Component.translatable("gui.textbook.import_clip"), b -> this.importClipboardText())
                        .pos(this.width / 2 - 120, 218)
                        .width(118)
                        .build()
        );
        this.volumeConfirmButton = (Button) this.addRenderableWidget(
                Button.builder(
                                Component.translatable(
                                        "gui.textbook.volume_confirm",
                                        selectedVolume,
                                        (int) Math.ceil(this.pages.size() / 100.0)
                                ),
                                b -> this.confirmVolumeSelection()
                        )
                        .pos(this.width / 2 + 100 + 2, 218)
                        .width(118)
                        .build()
        );
        this.upArrow = (Button) this.addRenderableWidget(
                Button.builder(Component.literal("^"), b -> {
                    ++selectedVolume;
                    this.updateButtonVisibility();
                })
                        .pos(this.width / 2 + 100 + 2, 198)
                        .width(20)
                        .build()
        );
        this.downArrow = (Button) this.addRenderableWidget(
                Button.builder(Component.literal("v"), b -> {
                    --selectedVolume;
                    this.updateButtonVisibility();
                })
                        .pos(this.width / 2 + 100 + 2, 238)
                        .width(20)
                        .build()
        );
        this.textbookButtonsInitialized = true;
        this.updateButtonVisibility();
    }

    @Inject(method = "updateButtonVisibility", at = @At("TAIL"))
    private void textbook$updateButtonVisibility(CallbackInfo ci) {
        if (!this.textbookButtonsInitialized) {
            return;
        }
        this.volumeConfirmButton.setMessage(
                Component.translatable(
                        "gui.textbook.volume_confirm",
                        selectedVolume,
                        (int) Math.ceil(this.pages.size() / 100.0)
                )
        );
        this.importClipboardButton.visible = !this.isSigning;
        this.importButton.visible = this.importClipboardButton.visible;
        int totalVolumeCount = (int) Math.ceil(this.pages.size() / 100.0);
        boolean showVolumeSelector = !this.isSigning && totalVolumeCount > 1;
        this.downArrow.visible = showVolumeSelector;
        this.volumeConfirmButton.visible = showVolumeSelector;
        this.upArrow.visible = showVolumeSelector;
        if (showVolumeSelector) {
            if (selectedVolume > totalVolumeCount) {
                selectedVolume = 1;
            }
            this.upArrow.active = selectedVolume < totalVolumeCount;
            this.downArrow.active = selectedVolume > 1;
        }
    }

    private void confirmVolumeSelection() {
        int maxVolumeDigits = String.valueOf((int) Math.ceil(this.pages.size() / 100.0)).length();
        this.setPages(Lists.partition(this.pages, 100).get(selectedVolume - 1));
        this.appendVolumeToTitle(maxVolumeDigits);
        this.updateButtonVisibility();
    }

    private void appendVolumeToTitle(int digits) {
        if (this.title.isEmpty()) {
            return;
        }
        if (digits > 15) {
            Textbook.LOGGER.info(
                    "Do you really need to import that much text? That's a {} digit number. "
                            + "How will you even label the volumes accurately?",
                    digits
            );
            digits = 15;
        }
        if (this.title.length() > 15 - digits) {
            this.title = this.title.substring(0, 15 - digits);
        }
        if (!this.title.isEmpty()) {
            this.title = this.title + "-" + String.format("%0" + digits + "d", selectedVolume);
        }
    }

    private void importFileText() {
        ImportBook.Response importedData = this.importBook.importBookFromFile();
        this.processImportResponse(importedData);
    }

    private void importClipboardText() {
        ImportBook.Response importedData = this.importBook.importBookFromClipboard();
        this.processImportResponse(importedData);
    }

    private void processImportResponse(ImportBook.Response response) {
        if (!response.success()) {
            return;
        }
        if (!response.title().isBlank()) {
            this.title = response.title();
        }
        this.setPages(response.pages());
    }

    private void setPages(List<String> newPages) {
        this.currentPage = 0;
        this.pages = newPages;
        this.clearDisplayCache();
        this.pageEdit.setCursorToEnd();
        this.isModified = true;
        this.clearDisplayCacheAfterPageChange();
        this.updateButtonVisibility();
    }
}
