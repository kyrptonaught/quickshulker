package net.kyrptonaught.quickshulker.mixin;

import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.shulkerutils.ItemStackInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStackInventory.class)
public class ItemStackInventoryMixin {

    @Shadow @Final private ItemStack itemStack;

    @Inject(method = "onClose", at = @At("TAIL"))
    public void removeQuickshulkerTag(PlayerEntity playerEntity_1, CallbackInfo ci){
        itemStack.removeSubTag(QuickShulkerMod.MOD_ID);
    }
}
