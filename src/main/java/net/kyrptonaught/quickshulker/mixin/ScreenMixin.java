package net.kyrptonaught.quickshulker.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.client.ClientUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
@Environment(EnvType.CLIENT)
public abstract class ScreenMixin {
    @Shadow
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected PlayerInventory playerInventory;

    @Shadow
    @Final
    protected ScreenHandler handler;

    @Inject(method = "init", at = @At("TAIL"))
    private void fixMouse(CallbackInfo ci) {
        if (QuickShulkerMod.lastMouseX != 0 && QuickShulkerMod.lastMouseY != 0) {
            GLFW.glfwSetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), QuickShulkerMod.lastMouseX, QuickShulkerMod.lastMouseY);
            QuickShulkerMod.lastMouseY = 0;
            QuickShulkerMod.lastMouseX = 0;
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void QS$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.getConfig().keybingInInv) {
            if (ClientUtil.keycode.getCategory() == InputUtil.Type.KEYSYM && keyCode == ClientUtil.keycode.getCode()) {
                if (handleTrigger())
                    cir.cancel();
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void QS$mousePressed(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.getConfig().rightClickInv) {
            if (playerInventory.getCursorStack().isEmpty() && button == 1) {
                if (handleTrigger())
                    cir.cancel();
            }
        }
        if (QuickShulkerMod.getConfig().keybingInInv) {
            if (ClientUtil.keycode.getCategory() == InputUtil.Type.MOUSE && button == ClientUtil.keycode.getCode()) {
                if (handleTrigger())
                    cir.cancel();
            }
        }
    }

    @Unique
    private boolean handleTrigger() {
        if (this.focusedSlot != null) {
            if (handler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
                if (((CreativeInventoryScreen) (Object) this).getSelectedTab() == ItemGroup.INVENTORY.getIndex()) {
                    return isValid(this.focusedSlot.getStack(), ((CreativeSlotMixin) this.focusedSlot).getSlot().id, 1);
                } else {
                    return isValid(this.focusedSlot.getStack(), this.focusedSlot.id - 9, 1);
                }
            }
            return isValid(this.focusedSlot.getStack(), this.focusedSlot.id, 0);
        }
        return false;
    }

    @Unique
    private boolean isValid(ItemStack stack, int id, int type) {
        if (this.focusedSlot.inventory instanceof PlayerInventory || (QuickShulkerMod.getConfig().rightClickInChest && this.focusedSlot.inventory instanceof SimpleInventory))
            if (ClientUtil.CheckAndSend(stack, id, type)) {
                QuickShulkerMod.lastMouseX = MinecraftClient.getInstance().mouse.getX();
                QuickShulkerMod.lastMouseY = MinecraftClient.getInstance().mouse.getY();
                return true;
            }
        return false;
    }
}
