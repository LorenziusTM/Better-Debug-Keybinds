package com.lorenzius.betterdebugkeybinds.client.action;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public record DebugAction(KeyMapping keyMapping, Consumer<Minecraft> executor, boolean repeatWhileHeld) {
	public void execute(Minecraft client) {
		executor.accept(client);
	}
}
