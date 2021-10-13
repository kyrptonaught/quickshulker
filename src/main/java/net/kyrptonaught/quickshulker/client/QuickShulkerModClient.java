package net.kyrptonaught.quickshulker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.quickshulker.OpenInventoryPacket;
import net.kyrptonaught.quickshulker.QuickBundlePacket;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulkerClient;
import net.kyrptonaught.quickshulker.mixin.CreativeSlotMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

@Environment(EnvType.CLIENT)
public class QuickShulkerModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            if (MinecraftClient.getInstance().currentScreen == null && QuickShulkerMod.getConfig().keybind) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (ClientUtil.isKeybindPressed()) {
                    //todo work with offhand
                    ClientUtil.CheckAndSend(player.getMainHandStack(), 36 + player.getInventory().selectedSlot);
                }
            }
        });
        ClientPlayNetworking.registerGlobalReceiver(OpenInventoryPacket.OPEN_INV, (client, handler, packet, sender) -> {
            client.execute(() -> {
                client.setScreen(new InventoryScreen(client.player));
            });
        });
        FabricLoader.getInstance().getEntrypoints(QuickShulkerMod.MOD_ID + "_client", RegisterQuickShulkerClient.class).forEach(RegisterQuickShulkerClient::registerClient);
    }
}