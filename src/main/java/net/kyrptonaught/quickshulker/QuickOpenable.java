package net.kyrptonaught.quickshulker;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface QuickOpenable {
    void quickShulker$QuickAccess(PlayerEntity playerEntity, ItemStack usedStack);
}
