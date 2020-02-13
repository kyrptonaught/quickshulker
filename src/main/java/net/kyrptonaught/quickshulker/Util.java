package net.kyrptonaught.quickshulker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import org.lwjgl.glfw.GLFW;

public class Util {
    public static boolean CheckAndSend(PlayerInventory playerInv, ItemStack stack) {
        if (isOpenableItem(stack)) {
            SendOpenPacket(playerInv, stack);
            return true;
        }
        return false;
    }

    private static Boolean isOpenableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof BlockItem &&
                (((BlockItem) item).getBlock() instanceof ShulkerBoxBlock || ((BlockItem) item).getBlock() instanceof EnderChestBlock);
    }

    private static void SendOpenPacket(PlayerInventory playerInv, ItemStack stack) {
        OpenShulkerPacket.sendOpenPacket(playerInv.getSlotWithStack(stack));
    }

    static ShulkerBoxContainer getContainer(int id, PlayerEntity player, int invSlot) {
        ItemStack stack = player.inventory.getInvStack(invSlot);
        CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(27, ItemStack.EMPTY);
        if (compoundTag != null && compoundTag.contains("Items", 9)) {
            Inventories.fromTag(compoundTag, itemStacks);
        }
        return new ShulkerBoxContainer(id, player.inventory, new ShulkerInventory(stack, itemStacks));
    }

    public static InputUtil.KeyCode keycode;

    @Environment(EnvType.CLIENT)
    public static boolean isKeybindPressed() {
        if (keycode == null)
            keycode = InputUtil.fromName(QuickShulkerMod.config.getConfig().keybinding);
        if (keycode.getCategory() == InputUtil.Type.MOUSE)
            return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getKeyCode()) == 1;
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getKeyCode()) == 1;
    }
}
