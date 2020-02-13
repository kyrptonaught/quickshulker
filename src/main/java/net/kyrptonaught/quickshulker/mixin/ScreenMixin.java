package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.Util;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerScreen.class)
public abstract class ScreenMixin {
    @Shadow
    protected Slot focusedSlot;

    @Shadow
    @Final
    protected PlayerInventory playerInventory;

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void QS$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.config.getConfig().keybingInInv)
            if (Util.keycode.getCategory() == InputUtil.Type.KEYSYM && keyCode == Util.keycode.getKeyCode()) {
                if (this.focusedSlot != null) {
                    Util.CheckAndSend(this.playerInventory, this.focusedSlot.getStack());
                    cir.cancel();
                }
            }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void QS$mousePressed(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (QuickShulkerMod.config.getConfig().rightClickInv)
            if (playerInventory.getCursorStack().isEmpty() && button == 1) {
                if (this.focusedSlot != null)
                    if (Util.CheckAndSend(this.playerInventory, this.focusedSlot.getStack()))
                        cir.cancel();
            }
        if (QuickShulkerMod.config.getConfig().keybingInInv)
            if (Util.keycode.getCategory() == InputUtil.Type.MOUSE && button == Util.keycode.getKeyCode()) {
                if (this.focusedSlot != null) {
                    if (Util.CheckAndSend(this.playerInventory, this.focusedSlot.getStack()))
                        cir.cancel();
                }
            }
    }
}
