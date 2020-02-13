package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.kyrptonaught.quickshulker.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class QuickShulkerMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "quickshulker";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitialize() {
        config.loadConfig();
        OpenShulkerPacket.registerReceivePacket();
    }

    @Override
    public void onInitializeClient() {
        WorldTickCallback.EVENT.register(world -> {
            if (MinecraftClient.getInstance().currentScreen == null && config.getConfig().keybind) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (Util.isKeybindPressed()) {
                    Util.CheckAndSend(player.inventory, player.getMainHandStack());
                }
            }
        });
    }
}