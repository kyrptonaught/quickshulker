package net.kyrptonaught.quickshulker.api;

import net.kyrptonaught.quickshulker.ItemInventoryContainer;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.block.Block;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;

public class Util {

    public static void openItem(PlayerEntity player, int invSlot) {
        openItem(player, invSlot, player.currentScreenHandler.slots.get(invSlot).getIndex());
    }

    public static void openItem(PlayerEntity player, int invSlot, int playerInvIndex) {
        ItemStack stack = player.getInventory().getStack(playerInvIndex);
        Block item = Block.getBlockFromItem(stack.getItem());
        stack.removeSubNbt(QuickShulkerMod.MOD_ID);
        if (QuickOpenableRegistry.quickies.containsKey(item.getClass())) {
            QuickOpenableRegistry.quickies.get(item.getClass()).consumer().accept(player, stack);
            ((ItemInventoryContainer) player.currentScreenHandler).setUsedSlot(playerInvIndex);
            player.currentScreenHandler.addListener(forceCloseScreenIfNotPresent(player, playerInvIndex, stack));
        }
    }

    public static Boolean isOpenableItem(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        Block block = ((BlockItem) item).getBlock();
        if (!QuickOpenableRegistry.quickies.containsKey(block.getClass()))
            return false;
        QuickShulkerData data = QuickOpenableRegistry.quickies.get(block.getClass());
        return !data.requiresSingularStack() || stack.getCount() <= 1;
    }

    public static Boolean isBundleableItem(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        Block block = ((BlockItem) item).getBlock();
        if (!QuickOpenableRegistry.quickies.containsKey(block.getClass()))
            return false;
        QuickShulkerData data = QuickOpenableRegistry.quickies.get(block.getClass());
        return data.supportsBundleing();
    }

    public static boolean isEnderChest(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        return ((BlockItem) item).getBlock() instanceof EnderChestBlock;
    }

    public static boolean isCraftingTable(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        return ((BlockItem) item).getBlock() instanceof CraftingTableBlock;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areNbtEqual(stack1, stack2) && stack1.getCount() == stack2.getCount();
    }

    public static ScreenHandlerListener forceCloseScreenIfNotPresent(PlayerEntity player, int slotID, ItemStack stack) {
        return new ScreenHandlerListener() {
            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                isValid();
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                isValid();
            }

            public void isValid() {
                if (!areItemsEqual(stack, player.getInventory().getStack(slotID))) {
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new CloseScreenS2CPacket(player.currentScreenHandler.syncId));
                    player.currentScreenHandler.close(player);
                    player.currentScreenHandler = player.playerScreenHandler;
                }
            }
        };
    }
}