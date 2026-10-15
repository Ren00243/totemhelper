package com.totemcursor;

import com.totemcursor.mixin.HandledScreenAccessor;
import com.totemcursor.mixin.MouseAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

public class TotemCursorMod implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof HandledScreen<?> handled) {
				moveCursorToTotem(client, handled);
			}
		});
	}

	private static void moveCursorToTotem(MinecraftClient client, HandledScreen<?> screen) {
		HandledScreenAccessor acc = (HandledScreenAccessor) screen;
		double scale = client.getWindow().getScaleFactor();

		// Punto de referencia: donde esta el cursor ahora (en coordenadas GUI)
		double curX = client.mouse.getX() / scale;
		double curY = client.mouse.getY() / scale;

		Slot best = null;
		double bestDist = Double.MAX_VALUE;

		for (Slot slot : screen.getScreenHandler().slots) {
			if (!slot.getStack().isOf(Items.TOTEM_OF_UNDYING)) continue;
			// Ignorar el offhand: ahi ya esta equipado
			if (slot.inventory instanceof PlayerInventory && slot.getIndex() == 40) continue;

			double sx = acc.totemcursor$getX() + slot.x + 8;
			double sy = acc.totemcursor$getY() + slot.y + 8;
			double dist = (sx - curX) * (sx - curX) + (sy - curY) * (sy - curY);
			if (dist < bestDist) {
				bestDist = dist;
				best = slot;
			}
		}

		if (best == null) return;

		double guiX = acc.totemcursor$getX() + best.x + 8;
		double guiY = acc.totemcursor$getY() + best.y + 8;
		double px = guiX * scale;
		double py = guiY * scale;

		// Actualizar el estado interno del mouse de Minecraft y el cursor real
		MouseAccessor mouse = (MouseAccessor) client.mouse;
		mouse.totemcursor$setX(px);
		mouse.totemcursor$setY(py);
		GLFW.glfwSetCursorPos(client.getWindow().getHandle(), px, py);
	}
}
