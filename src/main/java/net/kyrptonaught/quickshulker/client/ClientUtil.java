package net.kyrptonaught.quickshulker.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.OpenShulkerPacket;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientUtil {

    public static boolean CheckAndSend(ItemStack stack, int slot, int type) {
        if (Util.isOpenableItem(stack)) {
            SendOpenPacket(slot, type);
            return true;
        }
        return false;
    }

    private static void SendOpenPacket(int slot, int type) {
        OpenShulkerPacket.sendOpenPacket(slot, type);
    }

    public static InputUtil.Key keycode;

    public static boolean isKeybindPressed() {
        if (keycode == null) {
            keycode = InputUtil.fromTranslationKey(QuickShulkerMod.getConfig().keybinding);
        }
        if (keycode.getCategory() == InputUtil.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
        }
        return GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), keycode.getCode()) == 1;
    }
}
