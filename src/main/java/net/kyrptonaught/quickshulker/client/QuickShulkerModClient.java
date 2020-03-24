package net.kyrptonaught.quickshulker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulkerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

@Environment(EnvType.CLIENT)
public class QuickShulkerModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        WorldTickCallback.EVENT.register(world -> {
            if (MinecraftClient.getInstance().currentScreen == null && QuickShulkerMod.getConfig().keybind) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (ClientUtil.isKeybindPressed()) {
                    ClientUtil.CheckAndSend(player.inventory, player.getMainHandStack());
                }
            }
        });
        FabricLoader.getInstance().getEntrypoints(QuickShulkerMod.MOD_ID + "_client", RegisterQuickShulkerClient.class).forEach(RegisterQuickShulkerClient::registerClient);
    }
}
