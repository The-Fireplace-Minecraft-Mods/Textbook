package the_fireplace.textbook.mixin;

import net.minecraft.client.gui.screen.ingame.BookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookScreen.class)
public abstract class BookScreenMixin {
	@Shadow private BookScreen.Contents contents;

	@Inject(at = @At(value="HEAD"), method = "keyPressed")
	private void keyPressed(CallbackInfoReturnable<Boolean> infoReturnable) {

	}
}
