package net.kyrptonaught.quickshulker.api;


import net.kyrptonaught.quickshulker.QuickShulkerMod;
import net.kyrptonaught.shulkerutils.ShulkerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public class QuickShulkerData {
    public BiConsumer<PlayerEntity, ItemStack> openConsumer;
    public Boolean supportsBundleing = false;
    public Boolean ignoreSingleStackCheck = false;

    public QuickShulkerData() {

    }

    public QuickShulkerData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean supportsBundleing) {
        this.openConsumer = openConsumer;
        this.supportsBundleing = supportsBundleing;
    }

    public QuickShulkerData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean supportsBundleing, Boolean ignoreSingleStackCheck) {
        this.openConsumer = openConsumer;
        this.supportsBundleing = supportsBundleing;
        this.ignoreSingleStackCheck = ignoreSingleStackCheck;
    }

    public Inventory getInventory(PlayerEntity player, ItemStack stack) {
        return ShulkerUtils.getInventoryFromShulker(stack);
    }

    public static class QuickEnderData extends QuickShulkerData {
        public QuickEnderData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean supportsBundleing) {
            super(openConsumer, supportsBundleing);
        }

        public QuickEnderData(BiConsumer<PlayerEntity, ItemStack> openConsumer, Boolean supportsBundleing, Boolean ignoreSingleStackCheck) {
            super(openConsumer, supportsBundleing, ignoreSingleStackCheck);
        }

        public Inventory getInventory(PlayerEntity player, ItemStack stack) {
            if (!QuickShulkerMod.getConfig().quickEChest)
                return null;
            return player.getEnderChestInventory();
        }
    }
}
