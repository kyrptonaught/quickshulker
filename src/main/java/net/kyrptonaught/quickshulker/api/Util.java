package net.kyrptonaught.quickshulker.api;

import net.kyrptonaught.quickshulker.ItemInventoryContainer;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.minecraft.block.Block;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Util {
    public static void openItem(PlayerEntity player, ItemStack stack) {
        Block item = ((BlockItem) stack.getItem()).getBlock();
        if (item instanceof EnderChestBlock) stack.removeSubTag(QuickShulkerMod.MOD_ID);
        else
            stack.getOrCreateSubTag(QuickShulkerMod.MOD_ID).putBoolean("opened", true);

        QuickOpenableRegistry.consumers.get(item.getClass()).accept(player, stack);
        ((ItemInventoryContainer) player.currentScreenHandler).setOpenedItem(stack);
    }

    public static void openItem(PlayerEntity player, int invSlot) {
        openItem(player, player.inventory.getStack(invSlot));
    }

    public static Boolean isOpenableItem(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof BlockItem)) return false;
        Block block = ((BlockItem) item).getBlock();
        if (!(block instanceof EnderChestBlock) && stack.getCount() != 1) return false;
        return QuickOpenableRegistry.consumers.containsKey(block.getClass());
    }

    //Copied from net.minecraft.entity.player.PlayerInventory.getSlotWithStack
    public static int getSlotWithStack(PlayerInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.main.size(); ++i) {
            if (!inventory.main.get(i).isEmpty() && areItemsEqual(stack, inventory.main.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && ItemStack.areTagsEqual(stack1, stack2);
    }
}
