package net.kyrptonaught.quickshulker.optigui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import opekope2.optifinecompat.properties.ChestProperties;
import opekope2.optifinecompat.properties.OptiFineProperties;
import opekope2.optifinecompat.properties.ShulkerBoxProperties;
import opekope2.optigui.EntryPoint;
import opekope2.optigui.InitializerContext;
import opekope2.optigui.interaction.InteractionTarget;
import opekope2.optigui.service.InteractionService;
import opekope2.optigui.service.RegistryLookupService;
import opekope2.optigui.service.Services;
import org.jetbrains.annotations.NotNull;

public class Initializer implements EntryPoint {
    @Override
    public void onInitialize(@NotNull InitializerContext initializerContext) {
        InteractionWrapper.setImpl(new InteractionWrapper() {
            private final InteractionService interactionService = Services.getService(InteractionService.class);

            @Override
            public void interact(@NotNull PlayerEntity player, @NotNull World world, @NotNull Hand hand, @NotNull ItemStack item) {
                interactionService.interact(player, world, hand, new InteractionTarget.Preprocessed(getInteractionTargetData(item)), null);
            }
        });
    }

    private static Object getInteractionTargetData(ItemStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        RegistryLookupService lookup = Services.getService(RegistryLookupService.class);

        String container = stack.getItem().toString();
        Identifier biome = lookup.lookupBiome(client.world, client.player.getBlockPos());
        String name = stack.hasCustomName() ? stack.getName().getString() : null;

        return switch (container) {
            case "shulker_box",
                    "white_shulker_box",
                    "orange_shulker_box",
                    "magenta_shulker_box",
                    "light_blue_shulker_box",
                    "yellow_shulker_box",
                    "lime_shulker_box",
                    "pink_shulker_box",
                    "gray_shulker_box",
                    "light_gray_shulker_box",
                    "cyan_shulker_box",
                    "purple_shulker_box",
                    "blue_shulker_box",
                    "brown_shulker_box",
                    "green_shulker_box",
                    "red_shulker_box",
                    "black_shulker_box" -> new ShulkerBoxProperties(
                    name,
                    biome,
                    client.player.getBlockY(),
                    getColorFromShulkerBox(container));
            case "ender_chest" -> new ChestProperties(
                    name,
                    biome,
                    client.player.getBlockY(),
                    false,
                    false,
                    false,
                    true,
                    false,
                    false);
            case "crafting_table", "stonecutter" -> new OptiFineProperties(
                    name,
                    biome,
                    client.player.getBlockY());
            default -> null;
        };
    }

    private static String getColorFromShulkerBox(String itemName) {
        assert itemName.endsWith("shulker_box");

        String color = itemName.substring(0, itemName.length() - "shulker_box".length());
        return color.isEmpty() ? null : color.substring(0, color.length() - 1);
    }
}
