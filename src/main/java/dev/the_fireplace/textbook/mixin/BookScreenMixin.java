package dev.the_fireplace.textbook.mixin;

import dev.the_fireplace.textbook.Textbook;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(BookScreen.class)
public abstract class BookScreenMixin extends Screen {
	protected BookScreenMixin(Text title) {
		super(title);
	}

	@Shadow private BookScreen.Contents contents;

	@Inject(at = @At(value="TAIL"), method = "init")
	private void init(CallbackInfo info) {
		this.addButton(new ButtonWidget(this.width / 2 + 2, 196 + 20 + 2, 98, 20, new TranslatableText("gui.textbook.export"), (buttonWidget) -> {
			File exportFile = Textbook.getLogic().fileSaveSelectionDialog();
			if (exportFile != null) {
				Textbook.getLogic().exportContents(exportFile, contents);
			}
		}));
	}
}
