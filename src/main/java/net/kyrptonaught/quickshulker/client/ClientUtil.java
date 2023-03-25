package net.kyrptonaught.quickshulker.client;

import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.mixin.CreativeSlotMixin;
import net.kyrptonaught.quickshulker.network.OpenShulkerPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import opekope2.optifinecompat.properties.ChestProperties;
import opekope2.optifinecompat.properties.OptiFineProperties;
import opekope2.optifinecompat.properties.ShulkerBoxProperties;
import opekope2.optigui.service.RegistryLookupService;
import opekope2.optigui.service.Services;

public class ClientUtil {

    public static boolean CheckAndSend(ItemStack stack, int slot) {
        if (Util.isOpenableItem(stack) && !Util.isBlockBlockingQuickOpen(MinecraftClient.getInstance().world, MinecraftClient.getInstance().player)) {
            SendOpenPacket(slot);
            return true;
        }
        return false;
    }

    private static void SendOpenPacket(int slot) {
        OpenShulkerPacket.sendOpenPacket(slot);
    }

    public static boolean isCreativeScreen(PlayerEntity player) {
        return player.currentScreenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler;

    }

    public static int getSlotId(ScreenHandler handler, Slot slot) {
        if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            if (((CreativeInventoryScreen) MinecraftClient.getInstance().currentScreen).isInventoryTabSelected() && slot instanceof CreativeInventoryScreen.CreativeSlot) {
                return ((CreativeSlotMixin) slot).getSlot().id;
            } else {
                return slot.id - 9;
            }
        }
        return slot.id;
    }

    public static int getPlayerInvSlot(ScreenHandler handler, Slot slot) {
        if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            if (((CreativeInventoryScreen) MinecraftClient.getInstance().currentScreen).isInventoryTabSelected() && slot instanceof CreativeInventoryScreen.CreativeSlot) {
                return ((CreativeSlotMixin) slot).getSlot().getIndex();
            }
        }
        return slot.getIndex();
    }

    public static Object getInteractionTargetData(ItemStack stack) {
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
