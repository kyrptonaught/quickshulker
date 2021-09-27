package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
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
        //System.out.println(player.world.isClient); why tf does this only get called client side in singleplayer?? Why mojank??
        if (clickType == ClickType.RIGHT)
            if (ShulkerUtils.isShulkerItem(stack) && Util.isBundleableItem(stack)) {
                cursorStackReference.set(ShulkerUtils.insertIntoShulker(ShulkerUtils.getInventoryFromShulker(stack), otherStack, player));
                cir.setReturnValue(true);
            }/* else if (Util.isEnderChest(stack)) {
                cursorStackReference.set(player.getEnderChestInventory().addStack(otherStack));
                cir.setReturnValue(true);
            }
            */
    }
}
