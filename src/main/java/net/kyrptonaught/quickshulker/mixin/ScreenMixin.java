package net.kyrptonaught.quickshulker.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.quickshulker.ItemInventoryContainer;
import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.client.ClientUtil;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void QS$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.getConfig().keybingInInv) {
            if (ClientUtil.keycode.getCategory() == InputUtil.Type.KEYSYM && keyCode == ClientUtil.keycode.getCode()) {
                if (this.focusedSlot != null) {
                    ClientUtil.CheckAndSend(this.playerInventory, this.focusedSlot.getStack());
                    cir.cancel();
                }
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void QS$mousePressed(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.getConfig().rightClickInv) {
            if (playerInventory.getCursorStack().isEmpty() && button == 1) {
                if (this.focusedSlot != null) {
                    if (ClientUtil.CheckAndSend(this.playerInventory, this.focusedSlot.getStack())) {
                        cir.cancel();
                    }
                }
            }
        }
        if (QuickShulkerMod.getConfig().keybingInInv) {
            if (ClientUtil.keycode.getCategory() == InputUtil.Type.MOUSE && button == ClientUtil.keycode.getCode()) {
                if (this.focusedSlot != null) {
                    if (ClientUtil.CheckAndSend(this.playerInventory, this.focusedSlot.getStack())) {
                        cir.cancel();
                    }
                }
            }
        }
    }
}
