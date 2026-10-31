package com.lorenzius.betterdebugkeybinds.client;

import com.lorenzius.betterdebugkeybinds.client.input.GlobalInputHandler;
import com.lorenzius.betterdebugkeybinds.client.keybind.KeybindManager;
import net.fabricmc.api.ClientModInitializer;

public class BetterDebugKeybindsClient implements ClientModInitializer {
	public static final String MOD_ID = "betterdebugkeybinds";

	@Override
	public void onInitializeClient() {
		KeybindManager.register();
		GlobalInputHandler.register();
	}
}
