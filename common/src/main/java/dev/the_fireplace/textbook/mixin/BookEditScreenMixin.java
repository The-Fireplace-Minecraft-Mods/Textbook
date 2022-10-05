package dev.the_fireplace.textbook.mixin;

import com.google.common.collect.Lists;
import dev.the_fireplace.textbook.TextbookConstants;
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

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {
	@Mutable
	@Shadow @Final private List<String> pages;

	@Shadow private String title;

    @Shadow
    private boolean isModified;

    @Shadow
    protected abstract void eraseEmptyTrailingPages();

    @Shadow
    protected abstract void clearDisplayCache();

    @Shadow
    private boolean isSigning;

    @Shadow
    protected abstract void updateButtonVisibility();

    @Shadow
    @Final
    private TextFieldHelper pageEdit;
    @Shadow
    private int currentPage;
    private Button importButton;
    private Button importClipboardButton;
    private Button volumeConfirmButton;
    private static int selectedVolume = 1;
    private Button upArrow;
    private Button downArrow;
    private boolean textbookButtonsInitialized = false;

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo info) {
        importButton = this.addRenderableWidget(new Button(this.width / 2 + 2, 196 + 20 + 2, 98, 20, Component.translatable("gui.textbook.import"), this::importFileText));
        importClipboardButton = this.addRenderableWidget(new Button(this.width / 2 - 120, 196 + 20 + 2, 118, 20, Component.translatable("gui.textbook.import_clip"), this::importClipboardText));
        volumeConfirmButton = this.addRenderableWidget(new Button(this.width / 2 + 100 + 2, 196 + 20 + 2, 118, 20, Component.translatable("gui.textbook.volume_confirm", selectedVolume, (int) Math.ceil(pages.size() / 100d)), this::confirmVolumeSelection));
        upArrow = this.addRenderableWidget(new Button(this.width / 2 + 100 + 2, 196 + 2, 20, 20, Component.nullToEmpty("^"), (buttonWidget) -> {
            selectedVolume++;
            updateButtonVisibility();
        }));
        downArrow = this.addRenderableWidget(new Button(this.width / 2 + 100 + 2, 196 + 40 + 2, 20, 20, Component.nullToEmpty("v"), (buttonWidget) -> {
            selectedVolume--;
            updateButtonVisibility();
        }));
        textbookButtonsInitialized = true;
        updateButtonVisibility();
    }

    @Inject(at = @At("TAIL"), method = "updateButtonVisibility")
    private void updateButtonVisibility(CallbackInfo ci) {
        if (textbookButtonsInitialized) {
            volumeConfirmButton.setMessage(Component.translatable("gui.textbook.volume_confirm", selectedVolume, (int) Math.ceil(pages.size() / 100d)));
            importButton.visible = importClipboardButton.visible = !this.isSigning;
            int totalVolumeCount = (int) Math.ceil(pages.size() / 100d);
            boolean showVolumeSelector = !this.isSigning && totalVolumeCount > 1;
            upArrow.visible = downArrow.visible = volumeConfirmButton.visible = showVolumeSelector;
            if (showVolumeSelector) {
                if (selectedVolume > totalVolumeCount) {
                    selectedVolume = 1;
                }
                upArrow.active = selectedVolume < totalVolumeCount;
                downArrow.active = selectedVolume > 1;
            }
		}
	}

    private void confirmVolumeSelection(Button buttonWidget) {
        int maxVolumeDigits = String.valueOf((int) Math.ceil(pages.size() / 100d)).length();
        setPages(Lists.partition(this.pages, 100).get(selectedVolume - 1));
        appendVolumeToTitle(maxVolumeDigits);
        updateButtonVisibility();
    }

	private void appendVolumeToTitle(int digits) {
		if (title.isEmpty()) {
			return;
		}
		if (digits > 15) {
			TextbookConstants.getLogger().info("Do you really need to import that much text? That's a {} digit number. How will you even label the volumes accurately?", digits);
            digits = 15;
        }
        if (title.length() > 15 - digits) {
            this.title = title.substring(0, 15 - digits);
        }
        if (!title.isEmpty()) {
            this.title += "-" + String.format("%0" + digits + "d", selectedVolume);
        }
    }

    private void importFileText(Button buttonWidget) {
        ImportBook importBook = TextbookConstants.getInjector().getInstance(ImportBook.class);
        ImportBook.Response importedData = importBook.importBookFromFile();
        processImportResponse(importedData);
    }

    private void importClipboardText(Button buttonWidget) {
        ImportBook importBook = TextbookConstants.getInjector().getInstance(ImportBook.class);
        ImportBook.Response importedData = importBook.importBookFromClipboard();
        processImportResponse(importedData);
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

	private void setPages(List<String> pages) {
        this.currentPage = 0;
        this.pages = pages;
        this.eraseEmptyTrailingPages();
        this.pageEdit.setCursorToEnd();
        this.isModified = true;
        this.clearDisplayCache();
        updateButtonVisibility();
    }
}
