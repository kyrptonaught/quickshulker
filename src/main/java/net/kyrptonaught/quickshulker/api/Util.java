package net.kyrptonaught.quickshulker.api;

import net.kyrptonaught.quickshulker.ItemInventoryContainer;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.network.OpenInventoryPacket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;

public class Util {

    public static void openItem(PlayerEntity player, int invSlot) {
        if (invSlot < 0) {
            System.out.println("[QuickShulker]: unknown slot opened");
            //return; //not preventing the crash might make it easier to debug a fix.
        }
        openItem(player, invSlot, player.currentScreenHandler.slots.get(invSlot).getIndex());
    }

    public static void openItem(PlayerEntity player, int invSlot, int playerInvIndex) {
        if (QuickShulkerMod.getConfig().rightClickClose && playerInvIndex == ((ItemInventoryContainer) player.currentScreenHandler).getUsedSlotInPlayerInv()) {
            ((ServerPlayerEntity) player).networkHandler.sendPacket(new CloseScreenS2CPacket(player.currentScreenHandler.syncId));
            player.currentScreenHandler.close(player);
            player.currentScreenHandler = player.playerScreenHandler;
            OpenInventoryPacket.send((ServerPlayerEntity) player);
            return;
        }
        ItemStack stack = player.getInventory().getStack(playerInvIndex);
        Block item = Block.getBlockFromItem(stack.getItem());
        stack.removeSubNbt(QuickShulkerMod.MOD_ID);
        if (QuickOpenableRegistry.quickies.containsKey(item.getClass())) {
            QuickOpenableRegistry.quickies.get(item.getClass()).openConsumer.accept(player, stack);
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
        return stack.getCount() <= 1;
    }

    public static Inventory getQuickItemInventory(PlayerEntity player, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (QuickOpenableRegistry.quickies.containsKey(block.getClass())) {
                QuickShulkerData data = QuickOpenableRegistry.quickies.get(block.getClass());
                if (data.supportsBundleing)
                    return data.getInventory(player, stack);
            }
        }
        return null;
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