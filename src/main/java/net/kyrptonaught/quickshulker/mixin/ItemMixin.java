package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.QuickBundlePacket;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.client.ClientUtil;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "onClicked", at = @At("HEAD"), cancellable = true)
    public void onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        //why tf does this only get called client side in singleplayer?? Why mojank??
        //wht tf does this not get called on the server in the creative inv, but does with every other inv????
        //System.out.println(player.world.isClient);
        if (clickType == ClickType.RIGHT && !stack.isEmpty() && !otherStack.isEmpty()) {
            if (Util.getQuickItemInventory(player, stack) != null && !ShulkerUtils.isShulkerItem(otherStack)) {
                if (!player.world.isClient) {
                    ItemStack output = QuickBundlePacket.bundleItem(player, stack, otherStack);
                    if (output != null)
                        cursorStackReference.set(output);
                } else if (slot.inventory instanceof PlayerInventory) {
                    ClientUtil.OnClickedBundleClientSideLogic(player, otherStack, slot);
                }
                cir.setReturnValue(true);
            }
        }
    }
}