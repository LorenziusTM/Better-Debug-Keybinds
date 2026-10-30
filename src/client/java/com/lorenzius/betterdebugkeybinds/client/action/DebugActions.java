package com.lorenzius.betterdebugkeybinds.client.action;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;

public final class DebugActions {
	private DebugActions() {
	}

	public static void reloadSounds(Minecraft client) {
		client.getSoundManager().reload();
	}

	public static void clearChat(Minecraft client) {
		client.gui.hud.getChat().clearMessages(false);
	}

	public static void toggleChunkBorders(Minecraft client) {
		if (client.player != null && !client.player.isReducedDebugInfo()) {
			client.debugEntries.toggleStatus(DebugScreenEntries.CHUNK_BORDERS);
		}
	}

	public static void toggleHitboxes(Minecraft client) {
		if (client.player != null && !client.player.isReducedDebugInfo()) {
			client.debugEntries.toggleStatus(DebugScreenEntries.ENTITY_HITBOXES);
		}
	}
}
