package the_fireplace.textbook.mixin;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookEditScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import the_fireplace.textbook.TextbookLogic;

import java.io.File;
import java.util.List;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {
	@Mutable
	@Shadow @Final private List<String> pages;

	@Shadow private String title;

	@Shadow private boolean dirty;

	@Shadow protected abstract void removeEmptyPages();

	@Shadow protected abstract void invalidatePageContent();

	@Shadow private boolean signing;

	@Shadow protected abstract void updateButtons();

	private ButtonWidget importButton;
	private ButtonWidget clipboardImportButton;
	private ButtonWidget volumeConfirmButton;
	private int selectedVolume = 1;
	private ButtonWidget upArrow;
	private ButtonWidget downArrow;
	private boolean textbookButtonsInitialized = false;

	protected BookEditScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At(value="TAIL"), method = "init")
	private void init(CallbackInfo info) {
		importButton = this.addButton(new ButtonWidget(this.width / 2 + 2, 196 + 20 + 2, 98, 20, new TranslatableText("gui.textbook.import"), (buttonWidget) -> {
			File importFile = TextbookLogic.getFile();
			if(importFile != null) {
				this.pages = TextbookLogic.toPages(TextbookLogic.importContents(importFile));
				this.removeEmptyPages();
				this.dirty = true;
				this.invalidatePageContent();
				//noinspection UnstableApiUsage
				this.title = Files.getNameWithoutExtension(importFile.getName());
				if(title.length() > 16)
					this.title = title.substring(0, 16);
				updateButtons();
			}
		}));
		clipboardImportButton = this.addButton(new ButtonWidget(this.width / 2 - 120, 196 + 20 + 2, 118, 20, new TranslatableText("gui.textbook.import_clip"), (buttonWidget) -> {
			assert this.client != null;
			this.pages = TextbookLogic.toPages(Lists.newArrayList(this.client.keyboard.getClipboard().split("\\R")));
			this.removeEmptyPages();
			this.dirty = true;
			this.invalidatePageContent();
			updateButtons();
		}));
		volumeConfirmButton = this.addButton(new ButtonWidget(this.width / 2 + 100 + 2, 196 + 20 + 2, 118, 20, new TranslatableText("gui.textbook.volume_confirm", selectedVolume, (int)Math.ceil(pages.size() / 100d)), (buttonWidget) -> {
			this.pages = Lists.partition(this.pages, 100).get(selectedVolume-1);
			this.dirty = true;
			this.invalidatePageContent();
			int maxVolume = (int)Math.ceil(pages.size() / 100d);
			if(title.length() > 15-String.valueOf(maxVolume).length())
				this.title = title.substring(0, 15-String.valueOf(maxVolume).length());
			this.title += "-"+String.format("%0"+String.valueOf(maxVolume).length()+"d", selectedVolume);
			updateButtons();
		}));
		upArrow = this.addButton(new ButtonWidget(this.width / 2 + 100 + 2, 196 + 2, 20, 20, Text.of("^"), (buttonWidget) -> {
			selectedVolume++;
			updateButtons();
		}));
		downArrow = this.addButton(new ButtonWidget(this.width / 2 + 100 + 2, 196 + 40 + 2, 20, 20, Text.of("v"), (buttonWidget) -> {
			selectedVolume--;
			updateButtons();
		}));
		textbookButtonsInitialized = true;
		updateButtons();
	}

	@Inject(at = @At("TAIL"), method = "updateButtons")
	private void updateButtons(CallbackInfo ci) {
		if(textbookButtonsInitialized) {
			volumeConfirmButton.setMessage(new TranslatableText("gui.textbook.volume_confirm", selectedVolume, (int)Math.ceil(pages.size() / 100d)));
			importButton.visible = !this.signing;
			upArrow.visible = downArrow.visible = volumeConfirmButton.visible = !this.signing && pages.size() > 100;
			int maxVolume = (int) Math.ceil(pages.size() / 100d);
			int minVolume = 1;
			upArrow.active = selectedVolume < maxVolume;
			downArrow.active = selectedVolume > minVolume;
		}
	}
}
