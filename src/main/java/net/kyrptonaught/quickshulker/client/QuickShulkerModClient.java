package net.kyrptonaught.quickshulker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.AddNonConflictingKeyBind;
import net.kyrptonaught.kyrptconfig.config.NonConflicting.NonConflictingKeyBindData;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulkerClient;
import net.kyrptonaught.quickshulker.network.OpenInventoryPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

@Environment(EnvType.CLIENT)
public class QuickShulkerModClient implements ClientModInitializer, AddNonConflictingKeyBind {
    public static KeyBinding quickKey;

    @Override
    public void onInitializeClient() {
        quickKey = new KeyBinding();
        quickKey.setRaw(QuickShulkerMod.getConfig().keybinding);
        ClientTickEvents.START_WORLD_TICK.register(clientWorld -> {
            if (MinecraftClient.getInstance().currentScreen == null && QuickShulkerMod.getConfig().keybind) {
                PlayerEntity player = MinecraftClient.getInstance().player;
                if (quickKey.isKeybindPressed() && player != null) {
                    if (player.getMainHandStack().isEmpty() && !player.getOffHandStack().isEmpty())
                        ClientUtil.CheckAndSend(player.getOffHandStack(), 45);
                    else
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

    @Override
    public void addKeyBinding(List<NonConflictingKeyBindData> list) {
        InputUtil.Key key = quickKey.getKeybinding();
        NonConflictingKeyBindData bindData = new NonConflictingKeyBindData("key.quickshulker.config.keybinding", "key.categories.quickshulker", key.getCategory(), key.getCode(), setKey -> {
            QuickShulkerMod.getConfig().keybinding = setKey.getTranslationKey();
            QuickShulkerMod.config.save();
            quickKey.setRaw(setKey.getTranslationKey());
        });
        list.add(bindData);
    }
}