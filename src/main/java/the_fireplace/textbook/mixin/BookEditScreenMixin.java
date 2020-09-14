package the_fireplace.textbook.mixin;

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

	protected BookEditScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At(value="TAIL"), method = "init")
	private void init(CallbackInfo info) {
		this.addButton(new ButtonWidget(this.width / 2 + 2, 196 + 20 + 2, 98, 20, new TranslatableText("gui.textbook.import"), (buttonWidget) -> {
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
			}
		}));
	}
}
