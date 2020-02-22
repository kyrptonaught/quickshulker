package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulkerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.TypedActionResult;

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
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getMainHandStack();
            if (QuickShulkerMod.config.getConfig().rightClickToOpen) {
                if (Util.CheckAndSend(player.inventory, stack))
                    return TypedActionResult.success(stack);
            }
            return TypedActionResult.pass(stack);
        });
        FabricLoader.getInstance().getEntrypoints(QuickShulkerMod.MOD_ID+"_client", RegisterQuickShulkerClient.class).forEach(RegisterQuickShulkerClient::registerClient);
    }
}
