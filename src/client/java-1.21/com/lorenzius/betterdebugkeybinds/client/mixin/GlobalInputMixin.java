package com.lorenzius.betterdebugkeybinds.client.mixin;

import com.lorenzius.betterdebugkeybinds.client.input.GlobalInputHandler;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class GlobalInputMixin {
	@Shadow @Final private Minecraft minecraft;

	@Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
	private void onKeyPress(
			long window, int action, @NonNull KeyEvent event, CallbackInfo callback) {
		if (GlobalInputHandler.handleKeyEvent(minecraft, action, event)) {
			callback.cancel();
		}
	}
}
