package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.quickshulker.api.Util;
import net.kyrptonaught.quickshulker.client.ClientUtil;
import net.kyrptonaught.quickshulker.network.QuickBundlePacket;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
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
    public void QS$onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference, CallbackInfoReturnable<Boolean> cir) {
        //wht tf does this not get called on the server in the creative inv, but does with every other inv???? The creative menu is horrible
        //System.out.println(player.world.isClient);
        if (clickType == ClickType.RIGHT && QuickShulkerMod.getConfig().supportsBundlingInsert && !stack.isEmpty() && !otherStack.isEmpty()) {
            if (Util.getQuickItemInventory(player, stack) != null && !ShulkerUtils.isShulkerItem(otherStack)) {
                if (!player.world.isClient) {
                    ItemStack output = QuickBundlePacket.bundleItem(player, stack, otherStack);
                    if (output != null) {
                        cursorStackReference.set(output);
                        cir.setReturnValue(true);
                    }
                } else if (slot.inventory instanceof PlayerInventory && ClientUtil.isCreativeScreen(player)) {//client in creative screen
                    QuickBundlePacket.sendPacket(ClientUtil.getPlayerInvSlot(player.currentScreenHandler, slot), otherStack);
                    ItemStack output = QuickBundlePacket.bundleItem(player, stack, otherStack);
                    if (output != null) {
                        cursorStackReference.set(output);
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }

    @Inject(method = "onStackClicked", at = @At("HEAD"), cancellable = true)
    public void QS$onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack otherStack = slot.getStack();
        if (clickType == ClickType.RIGHT && (QuickShulkerMod.getConfig().supportsBundlingPickup || QuickShulkerMod.getConfig().supportsBundlingextract) && !stack.isEmpty()) {
            if (Util.getQuickItemInventory(player, stack) != null && !ShulkerUtils.isShulkerItem(otherStack)) {
                if (!player.world.isClient) {
                    if (!otherStack.isEmpty()) { //bundle
                        if (QuickShulkerMod.getConfig().supportsBundlingPickup) {
                            ItemStack output = QuickBundlePacket.bundleItem(player, stack, otherStack);
                            if (output != null) {
                                cir.setReturnValue(true);
                            }
                        }
                    } else if (QuickShulkerMod.getConfig().supportsBundlingextract) {//unbundle
                        Inventory inv = Util.getQuickItemInventory(player, stack);
                        for (int i = inv.size() - 1; i >= 0; i--) {
                            if (!inv.getStack(i).isEmpty()) {
                                slot.setStack(inv.removeStack(i));
                                inv.onClose(player);
                                cir.setReturnValue(true);
                                return;
                            }
                        }
                    }
                } else if (slot.inventory instanceof PlayerInventory && ClientUtil.isCreativeScreen(player)) { //client in creative screen
                    if (!otherStack.isEmpty()) {//bundle
                        if (QuickShulkerMod.getConfig().supportsBundlingPickup) {
                            QuickBundlePacket.sendPacket(ClientUtil.getPlayerInvSlot(player.currentScreenHandler, slot), otherStack); //todo fix this
                            ItemStack output = QuickBundlePacket.bundleItem(player, stack, otherStack);
                            if (output != null) {
                                QuickBundlePacket.sendCreativeSlotUpdate(output, slot);
                                cir.setReturnValue(true);
                            }
                        }
                    } else if (QuickShulkerMod.getConfig().supportsBundlingextract) {//unbundle
                        QuickBundlePacket.Unbundle.sendPacket(ClientUtil.getPlayerInvSlot(player.currentScreenHandler, slot), stack);
                        Inventory inv = Util.getQuickItemInventory(player, stack);
                        for (int i = inv.size() - 1; i >= 0; i--) {
                            if (!inv.getStack(i).isEmpty()) {
                                slot.setStack(inv.removeStack(i));
                                inv.onClose(player);
                                cir.setReturnValue(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}