package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class QuickShulkerModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldTickCallback.EVENT.register(world -> {
            if (MinecraftClient.getInstance().currentScreen == null && QuickShulkerMod.config.getConfig().keybind) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (Util.isKeybindPressed()) {
                    Util.CheckAndSend(player.inventory, player.getMainHandStack());
                }
            }
        });
    }
}
