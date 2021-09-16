package net.kyrptonaught.quickshulker.api;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.function.BiConsumer;

public record QuickShulkerData(BiConsumer<PlayerEntity, ItemStack> consumer, Boolean requiresSingularStack, Boolean supportsBundleing) {
}
