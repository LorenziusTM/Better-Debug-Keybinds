package com.lorenzius.betterdebugkeybinds.client.input;

import com.lorenzius.betterdebugkeybinds.client.action.DebugAction;
import com.lorenzius.betterdebugkeybinds.client.keybind.KeybindManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;

public final class GlobalInputHandler {
	private static Integer heldKey;

	private GlobalInputHandler() {
	}

	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(GlobalInputHandler::onClientTick);
	}

	public static boolean handleKeyEvent(Minecraft client, int action, KeyEvent event) {
		if (action == InputConstants.RELEASE) {
			if (heldKey != null && heldKey == event.key()) {
				heldKey = null;
			}
			return false;
		}

		if (client.gui.screen() instanceof KeyBindsScreen) {
			return false;
		}

		if (action == InputConstants.PRESS) {
			boolean matched = false;
			for (DebugAction debugAction : KeybindManager.actions()) {
				if (debugAction.keyMapping().matches(event)) {
					debugAction.execute(client);
					if (debugAction.repeatWhileHeld()) {
						heldKey = event.key();
					}
					matched = true;
				}
			}
			return matched;
		}

		return action == InputConstants.REPEAT && KeybindManager.actions().stream()
				.anyMatch(debugAction -> debugAction.keyMapping().matches(event));
	}

	private static void onClientTick(Minecraft client) {
		if (heldKey == null) {
			return;
		}

		if (client.gui.screen() instanceof KeyBindsScreen || !InputConstants.isKeyDown(heldKey)) {
			heldKey = null;
			return;
		}

		for (DebugAction debugAction : KeybindManager.actions()) {
			if (debugAction.repeatWhileHeld() && debugAction.keyMapping().matches(
					InputConstants.Type.KEYBOARD.getOrCreate(heldKey)
			)) {
				debugAction.execute(client);
				return;
			}
		}
		heldKey = null;
	}
}
