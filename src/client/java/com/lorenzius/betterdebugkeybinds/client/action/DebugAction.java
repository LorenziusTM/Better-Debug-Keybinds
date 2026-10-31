package com.lorenzius.betterdebugkeybinds.client.action;

import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.jspecify.annotations.NonNull;

public record DebugAction(
		KeyMapping keyMapping, Consumer<@NonNull Minecraft> executor, boolean repeatWhileHeld) {
	public void execute(@NonNull Minecraft client) {
		executor.accept(client);
	}
}
