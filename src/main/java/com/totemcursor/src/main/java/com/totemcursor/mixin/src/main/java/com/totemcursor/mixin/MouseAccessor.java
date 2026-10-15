package com.totemcursor.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mouse.class)
public interface MouseAccessor {
	@Accessor("x")
	void totemcursor$setX(double x);

	@Accessor("y")
	void totemcursor$setY(double y);
}
