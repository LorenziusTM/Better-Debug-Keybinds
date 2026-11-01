package com.lorenzius.betterdebugkeybinds.client.keybind;

import com.lorenzius.betterdebugkeybinds.client.BetterDebugKeybindsClient;
import com.lorenzius.betterdebugkeybinds.client.action.DebugAction;
import com.lorenzius.betterdebugkeybinds.client.action.DebugActions;
import com.lorenzius.betterdebugkeybinds.client.action.ReloadChunksAction;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public final class KeybindManager {
	private static final KeyMapping.@NonNull Category CATEGORY =
			KeyMapping.Category.register(
					Identifier.fromNamespaceAndPath(
							BetterDebugKeybindsClient.MOD_ID, "debug_keybinds"));
	private static List<@NonNull DebugAction> actions = List.of();

	private KeybindManager() {}

	public static void register() {
		int unbound = InputConstants.UNKNOWN.getValue();
		KeyMapping reloadChunks = register("reload_chunks", unbound);
		KeyMapping reloadResourcePack = register("reload_resource_pack", unbound);
		KeyMapping clearChat = register("clear_chat", unbound);
		KeyMapping reloadSounds = register("reload_sounds", unbound);
		KeyMapping toggleChunkBorders = register("toggle_chunk_borders", unbound);
		KeyMapping toggleHitboxes = register("toggle_hitboxes", unbound);

		actions =
				List.of(
						new DebugAction(reloadChunks, ReloadChunksAction::execute, true),
						new DebugAction(
								reloadResourcePack,
								(Minecraft client) -> client.reloadResourcePacks(),
								false),
						new DebugAction(clearChat, DebugActions::clearChat, false),
						new DebugAction(reloadSounds, DebugActions::reloadSounds, false),
						new DebugAction(
								toggleChunkBorders, DebugActions::toggleChunkBorders, false),
						new DebugAction(toggleHitboxes, DebugActions::toggleHitboxes, false));
	}

	private static KeyMapping register(String action, int defaultKey) {
		return KeyMappingHelper.registerKeyMapping(
				new KeyMapping(
						"key.betterdebugkeybinds." + action,
						InputConstants.Type.KEYSYM,
						defaultKey,
						CATEGORY));
	}

	public static List<@NonNull DebugAction> actions() {
		return actions;
	}
}
