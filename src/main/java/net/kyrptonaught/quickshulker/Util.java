package net.kyrptonaught.quickshulker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.api.QuickOpenable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        return (((BlockItem) item).getBlock() instanceof QuickOpenable);
    }

    private static void SendOpenPacket(PlayerInventory playerInv, ItemStack stack) {
        OpenShulkerPacket.sendOpenPacket(playerInv.getSlotWithStack(stack));
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
