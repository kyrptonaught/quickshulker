package net.kyrptonaught.quickshulker.api;


import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class QuickShulkerData {
    public BiConsumer<PlayerEntity, ItemStack> openConsumer;
    public Boolean requiresSingularStack;
    public Boolean supportsBundleing;

    public QuickShulkerData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean requiresSingularStack,
                            Boolean supportsBundleing) {
        this.openConsumer = openConsumer;
        this.requiresSingularStack = requiresSingularStack;
        this.supportsBundleing = supportsBundleing;
    }

    public Inventory getInventory(PlayerEntity player, ItemStack stack) {
        return ShulkerUtils.getInventoryFromShulker(stack);
    }

    public static class QuickEnderData extends QuickShulkerData {
        public QuickEnderData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean requiresSingularStack, Boolean supportsBundleing) {
            super(openConsumer, requiresSingularStack, supportsBundleing);
        }

        public Inventory getInventory(PlayerEntity player, ItemStack stack) {
            return player.getEnderChestInventory();
        }
    }
}
