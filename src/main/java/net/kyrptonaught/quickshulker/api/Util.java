package net.kyrptonaught.quickshulker.api;

import net.kyrptonaught.quickshulker.ItemInventoryContainer;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.block.Block;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

public class Util {
    public static void openItem(PlayerEntity player, ItemStack stack) {
        Block item = Block.getBlockFromItem(stack.getItem());
        stack.removeSubTag(QuickShulkerMod.MOD_ID);
        if (QuickOpenableRegistry.consumers.containsKey(item.getClass())) {
            QuickOpenableRegistry.consumers.get(item.getClass()).accept(player, stack);
            ((ItemInventoryContainer) player.currentScreenHandler).setOpenedItem(stack);
            player.currentScreenHandler.addListener(forceCloseScreenIfNotPresent(player, stack));
        }
    }

    public static void openItem(PlayerEntity player, int invSlot, int type) {
        if (type == 0) {
            if (invSlot == -69)//nice
                openItem(player, player.getMainHandStack());
            else if (invSlot >= 0 && invSlot < player.currentScreenHandler.slots.size())
                openItem(player, player.currentScreenHandler.getSlot(invSlot).getStack());
        } else if (type == 1) {
            if (invSlot >= 0 && invSlot < player.playerScreenHandler.slots.size())
                openItem(player, player.playerScreenHandler.getSlot(invSlot).getStack());
        }
    }

    public static Boolean isOpenableItem(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        Block block = ((BlockItem) item).getBlock();
        if (!(block instanceof EnderChestBlock) && stack.getCount() != 1) return false;
        return QuickOpenableRegistry.consumers.containsKey(block.getClass());
    }

    public static boolean isEnderChest(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        return ((BlockItem) item).getBlock() instanceof EnderChestBlock;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areTagsEqual(stack1, stack2) && stack1.getCount() == stack2.getCount();
    }

    public static ScreenHandlerListener forceCloseScreenIfNotPresent(PlayerEntity player, ItemStack stack) {
        return new ScreenHandlerListener() {
            @Override
            public void onHandlerRegistered(ScreenHandler handler, DefaultedList<ItemStack> stacks) {
                isValid();
            }

            @Override
            public void onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack) {
                isValid();
            }

            @Override
            public void onPropertyUpdate(ScreenHandler handler, int property, int value) {
                isValid();
            }

            public void isValid() {
                if (!player.inventory.contains(stack)) {
                    ((ServerPlayerEntity) player).networkHandler.sendPacket(new CloseScreenS2CPacket(player.currentScreenHandler.syncId));
                    player.currentScreenHandler = player.playerScreenHandler;
                }
            }
        };
    }
}