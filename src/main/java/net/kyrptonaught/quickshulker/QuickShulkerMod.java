package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.config.ConfigManager;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.TypedActionResult;

public class QuickShulkerMod implements ModInitializer, RegisterQuickShulker {
    public static final String MOD_ID = "quickshulker";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitialize() {
        config.loadConfig();
        OpenShulkerPacket.registerReceivePacket();
        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getMainHandStack();
            if (!world.isClient) {
                if (QuickShulkerMod.config.getConfig().rightClickToOpen) {
                    if (Util.isOpenableItem(stack)) {
                        Util.openItem(player, Util.getSlotWithStack(player.inventory, stack));
                        return TypedActionResult.success(stack);
                    }
                }
            }
            return TypedActionResult.pass(stack);
        });
        FabricLoader.getInstance().getEntrypoints(MOD_ID, RegisterQuickShulker.class).forEach(RegisterQuickShulker::registerProviders);
    }

    @Override
    public void registerProviders() {
        QuickOpenableRegistry.register(ShulkerBoxBlock.class, ((player, stack) -> player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                new ShulkerBoxContainer(i, player.inventory, new ItemStackInventory(stack, 27)), new TranslatableText("container.shulkerBox")))));

        QuickOpenableRegistry.register(EnderChestBlock.class, ((player, stack) -> player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                GenericContainer.createGeneric9x3(i, playerInventory, player.getEnderChestInventory()), new TranslatableText("container.enderchest")))));
    }
}