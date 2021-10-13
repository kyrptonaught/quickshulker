package net.kyrptonaught.quickshulker.client;

import net.kyrptonaught.quickshulker.OpenShulkerPacket;
import net.kyrptonaught.quickshulker.QuickBundlePacket;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.mixin.CreativeSlotMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.glfw.GLFW;

public class ClientUtil {

    public static boolean CheckAndSend(ItemStack stack, int slot) {
        if (Util.isOpenableItem(stack)) {
            SendOpenPacket(slot);
            return true;
        }
        return false;
    }

    private static void SendOpenPacket(int slot) {
        OpenShulkerPacket.sendOpenPacket(slot);
    }

    //See ItemMixin
    public static void OnClickedBundleClientSideLogic(PlayerEntity player, ItemStack otherStack, Slot slot) {
        if(player.currentScreenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler)
        QuickBundlePacket.sendPacket(getSlotId(player.currentScreenHandler, slot), otherStack);
    }

    public static int getSlotId(ScreenHandler handler, Slot slot) {
        if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            if (((CreativeInventoryScreen) MinecraftClient.getInstance().currentScreen).getSelectedTab() == ItemGroup.INVENTORY.getIndex() && slot instanceof CreativeInventoryScreen.CreativeSlot) {
                return ((CreativeSlotMixin) slot).getSlot().id;
            } else {
                return slot.id - 9;
            }
        }
        return slot.id;
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
