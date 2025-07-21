package com.keyboarddetector.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// 真的需要用到这个吗?
@Mixin(MinecraftServer.class)
public class ExampleMixin {

    @Inject(at = @At("HEAD"), method = "loadLevel")
    private void init(CallbackInfo info) {
        // This code is injected into the start of MinecraftServer.loadWorld()V
    }
}