package net.kyrptonaught.quickshulker;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.quickshulker.api.ItemStackInventory;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;
import net.kyrptonaught.quickshulker.config.ConfigManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.container.SimpleNamedContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class QuickShulkerMod implements ModInitializer,RegisterQuickShulker {
    public static final String MOD_ID = "quickshulker";
    public static ConfigManager config = new ConfigManager();

    @Override
    public void onInitialize() {
        config.loadConfig();
        OpenShulkerPacket.registerReceivePacket();

        List<RegisterQuickShulker> apis = FabricLoader.getInstance().getEntrypoints(MOD_ID, RegisterQuickShulker.class);
        apis.forEach(RegisterQuickShulker::registerProviders);
    }

    @Override
    public void registerProviders() {
        QuickOpenableRegistry.register(Blocks.SHULKER_BOX, ((player, stack) -> {
            player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                    new ShulkerBoxContainer(i, player.inventory, new ItemStackInventory(stack, 27)), new TranslatableText("container.shulkerBox")));
        }));
        QuickOpenableRegistry.register(Blocks.ENDER_CHEST, ((player, stack) -> {
            player.openContainer(new SimpleNamedContainerFactory((i, playerInventory, playerEntity) ->
                    GenericContainer.createGeneric9x3(i, playerInventory, player.getEnderChestInventory()), new TranslatableText("container.enderchest")));
        }));
    }
}