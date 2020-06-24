package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.config.ConfigOptions;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.TypedActionResult;


public class QuickShulkerMod implements ModInitializer, RegisterQuickShulker {
    public static final String MOD_ID = "quickshulker";
    public static ConfigManager.SingleConfigManager config = new ConfigManager.SingleConfigManager(MOD_ID,new ConfigOptions());

    @Override
    public void onInitialize() {
        config.load();
        OpenShulkerPacket.registerReceivePacket();
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getMainHandStack();
            if (!world.isClient) {
                if (QuickShulkerMod.getConfig().rightClickToOpen) {
                    if (Util.isOpenableItem(stack)) {
                        Util.openItem(player, stack);
                        return TypedActionResult.success(stack);
                    }
                }
            }
            return TypedActionResult.pass(stack);
        });
        FabricLoader.getInstance().getEntrypoints(MOD_ID, RegisterQuickShulker.class).forEach(RegisterQuickShulker::registerProviders);
    }

    public static ConfigOptions getConfig() {
        return (ConfigOptions) config.getConfig();
    }

    @Override
    public void registerProviders() {
        QuickOpenableRegistry.register(ShulkerBoxBlock.class, ((player, stack) -> player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
                new ShulkerBoxScreenHandler(i, player.inventory, new ItemStackInventory(stack, 27)), new TranslatableText("container.shulkerBox")))));

        QuickOpenableRegistry.register(EnderChestBlock.class, ((player, stack) -> player.openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
                GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, player.getEnderChestInventory()), new TranslatableText("container.enderchest")))));
    }
}