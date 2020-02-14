package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ModInitializer;
import net.kyrptonaught.quickshulker.config.ConfigManager;

public class QuickShulkerMod implements ModInitializer {
    public static final String MOD_ID = "quickshulker";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitialize() {
        config.loadConfig();
        OpenShulkerPacket.registerReceivePacket();
    }
}