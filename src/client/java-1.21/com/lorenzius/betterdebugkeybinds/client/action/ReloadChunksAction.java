package com.lorenzius.betterdebugkeybinds.client.action;

import net.minecraft.client.Minecraft;

public final class ReloadChunksAction {
	private ReloadChunksAction() {}

	public static void execute(Minecraft client) {
		client.levelRenderer.allChanged();
	}
}
